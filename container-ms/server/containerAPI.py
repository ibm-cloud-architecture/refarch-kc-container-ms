from flask import Flask, Blueprint
from flask_restplus import Api, Resource, fields
from werkzeug.contrib.fixers import ProxyFix
from confluent_kafka import KafkaError, Producer, Consumer
import psycopg2

app = Flask(__name__)
blueprint = Blueprint('api', __name__, url_prefix='/container')
app.wsgi_app = ProxyFix(app.wsgi_app)
api = Api(blueprint, version='1.0', title='Container MS API',
    description='API for checking container data', doc='/api/'
)
app.register_blueprint(blueprint)

ns = api.namespace('container', description='Operations to get container data.')
nsg = api.namespace('general', description='General application checks')

container = api.model('container', {
	'ID': fields.String(readOnly = True, description = 'The task unique identifier'),
	'Temperature': fields.String(required = True, description = 'The task details'),
	'Amp': fields.String(required = True, description = 'The task details'),
	'CumlativePowerConsumption': fields.String(required = True, description = 'The task details'),
	'ContentType': fields.String(required = True, description = 'The task details'),
	'Humidity': fields.String(required = True, description = 'The task details'),
	'CO2': fields.String(required = True, description = 'The task details')
})

class containerActions(object):
    def __init__(self):
        self.counter = 0

    def getAllContainers(self):
        conn = databaseActions.createConnection(self)
        cur = conn.cursor()
        cur.execute("SELECT * FROM containers;")
        containers=cur.fetchall()
        cur.close()
        conn.close()
        if len(containers) == 0:
            return 'There are no containers there.'
        else:
            return containers

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

class databaseActions(object):
    def __init__(self):
        self.counter = 0

    def createConnection(self):
        try:
            conn=psycopg2.connect(
                host = "bd2d0216-0b7d-4575-8c0b-d2e934843e41.6131b73286f34215871dfad7254b4f7d.databases.appdomain.cloud",
                port = "31384",
                dbname = "ibmclouddb",
                user = "ibm_cloud_c9587d97_28f1_4da3_9254_dd56907ef40c",
                password = "2d1c5269de3ea3766a5a9329ef874bdc077e2e57a336a3ba2a4d95dad7b91fa3"
            )
            print ("Connected to the database")
            return conn
        except:
            print ("Unable to connect to the database")
            return "ERROR"

cActions = containerActions()

@ns.route('/')
class containerList(Resource):
    '''Shows a list of all the containers in the system.'''
    @ns.doc('list_containers')
    def get(self):
        '''Lists Containers'''
        return cActions.getAllContainers()

@ns.route('/<int:id>')
@ns.response(404, 'Container not found')
@ns.param('id', 'Container ID')
class Container(Resource):
    '''Funtions to set the status of a container'''
    @ns.expect(container)
    def get(self, id):
        '''Returns data about a specified container.'''
        return  cActions.getContainerByID(id)

@ns.route('/<string:city>')
@ns.response(404, 'City not found')
@ns.param('city', 'Container ID')
class Container(Resource):
    '''Funtions to set the status of a container'''
    @ns.expect(container)
    def get(self, city):
        '''Returns containers in the city specified.'''
        return cActions.getCityContainers(city)

@nsg.route('/healthcheck')
class generalChecks(Resource):
    '''Basic Application Checks'''
    @nsg.doc('healthcheck')
    def get(self):
        '''Runs a basic health check'''
        return {'status': 'UP'}, 200

if __name__ == '__main__':
    app.run(debug=True)
