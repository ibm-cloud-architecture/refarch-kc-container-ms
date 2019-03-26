from flask import Flask, Blueprint
from flask_restplus import Api, Resource, fields
from werkzeug.contrib.fixers import ProxyFix
from confluent_kafka import KafkaError, Producer, Consumer

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
	'ID': fields.Integer(readOnly = True, description = 'The task unique identifier'),
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
        return {"Type" : "Hello"}

    def getContainerByID(self, id):
        todo = self.get(id)
        self.todos.remove(todo)

    def getCityContainers(self, city):
        todo = self.get(id)
        self.todos.remove(todo)


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
