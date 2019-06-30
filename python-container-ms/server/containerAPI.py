from flask import Flask, Blueprint
from flask_restplus import Api, Resource, fields
from werkzeug.contrib.fixers import ProxyFix
from confluent_kafka import KafkaError, Producer, Consumer
import psycopg2, yaml


app = Flask(__name__)
blueprint = Blueprint('api', __name__, url_prefix='/containers')
app.wsgi_app = ProxyFix(app.wsgi_app)
api = Api(blueprint, version='1.0', title='Container MS API',
    description='API for checking container data', doc='/api/'
)
app.register_blueprint(blueprint)

ns = api.namespace('container', description='Operations to get container data.')
nsg = api.namespace('general', description='General application checks')

container = api.model('container', {
	'ID': fields.String(required = True, description = 'Container ID'),
	'Latitude': fields.Integer(required = True,description = 'Container current latitude'),
	'Longitude': fields.Integer(required = True,description = 'Container current longitude'),
	'Type': fields.String(required = True, description = 'Container type'),
	'Status': fields.String(required = True, description = 'Container status'),
	'CurrentCity': fields.String(required = True, description = 'City container is currently in'),
	'Brand': fields.String(required = True, description = 'Brand of the container'),
    'Capacity': fields.Integer(required = True,description = 'Capacity of the container')
})

#Function for loading an object with database credentials from a yaml file.
def loadDBConfig(filepath):
    with open(filepath,"r") as dbConfig:
        data = yaml.load(dbConfig)
    return data

class containerActions(object):
    def __init__(self):
        self.counter = 0
    
    #connects to the DB and queries for everything in the containers table.
    def getAllContainers(self):
        conn = databaseActions.createConnection(self)
        cur = conn.cursor()
        cur.execute("SELECT * FROM CONTAINERS;")
        containers=cur.fetchall()
        print('DATA', containers)
        cur.close()
        conn.close()
        if len(containers) == 0:
            return 'There are no containers there.'
        else:
            return containers
    
    #connects to the DB, passes in a container ID and queries the containers table for the container with that ID.
    def getContainerByID(self, id):
        conn = databaseActions.createConnection(self)
        cur = conn.cursor()
        cur.execute("SELECT * from container WHERE id=%s", (id))
        containers=cur.fetchall()
        cur.close()
        conn.close()
        if len(containers) == 0:
            return 'There is no container with that ID.'
        else:
            return containers
    
    #connects to the DB, passes in a city name and queries the containers table for containers in that city.
    def getCityContainers(self, city):
        conn = databaseActions.createConnection(self)
        cur = conn.cursor()
        cur.execute("SELECT * from container WHERE city=%s", (city))
        containers=cur.fetchall()
        cur.close()
        conn.close()
        if len(containers) == 0:
            return 'There is no containers in the specified city.'
        else:
            return containers

#Class for actions related to the database.
class databaseActions(object):
    def __init__(self):
        self.counter = 0
    #Function for loading an object with database credentials from a yaml file.
    def createConnection(self):
            dbConfig = loadDBConfig('dbConfig.yaml')
            try:
                conn=psycopg2.connect(
                    host = dbConfig['containerDB']['host'],
                    port = dbConfig['containerDB']['port'],
                    dbname = dbConfig['containerDB']['dbname'],
                    user = dbConfig['containerDB']['user'],
                    password = dbConfig['containerDB']['password']
                )
                print ("Connected to the database")
                return conn
            except:
                print ("Unable to connect to the database")
                return "ERROR"

cActions = containerActions()

#GET endpoint for getting all of the containers.
@ns.route('/')
class containerList(Resource):
    '''Shows a list of all the containers in the system.'''
    @ns.doc('list_containers')
    def get(self):
        '''Lists Containers'''
        return cActions.getAllContainers()

#GET endpoint for getting a container based on it's ID.
@ns.route('/<int:id>')
@ns.response(404, 'Container not found')
@ns.param('id', 'Container ID')
class Container(Resource):
    '''Funtions to set the status of a container'''
    @ns.expect(container)
    def get(self, id):
        '''Returns data about a specified container.'''
        return  cActions.getContainerByID(id)

#GET endpoint for getting containers based on the city they are located.
@ns.route('/<string:city>')
@ns.response(404, 'City not found')
@ns.param('city', 'Container ID')
class Container(Resource):
    '''Funtions to set the status of a container'''
    @ns.expect(container)
    def get(self, city):
        '''Returns containers in the city specified.'''
        return cActions.getCityContainers(city)

#Endpoint for checking the health of the application.
@nsg.route('/healthcheck')
class generalChecks(Resource):
    '''Basic Application Checks'''
    @nsg.doc('healthcheck')
    def get(self):
        '''Runs a basic health check'''
        return {'status': 'UP'}, 200

#main function
if __name__ == '__main__':
    app.run(debug=True)
