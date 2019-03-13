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

containerConsumer = KafkaConsumer('container',
                         group_id='my-group',
                         bootstrap_servers=['localhost:9092'])

for message in containerConsumer:
    # message value and key are raw bytes -- decode if necessary!
    # e.g., for unicode: `message.value.decode('utf-8')`
    print ("%s:%d:%d: key=%s value=%s" % (message.topic, message.partition,
                                          message.offset, message.key,
                                          message.value))


@ns.route('/')
class containerList(Resource):
    '''Shows a list of all the containers in the system.'''
    @ns.doc('list_containers')
    def get(self):
        '''Lists Container Data'''
        return {'task': 'hello'}

    @ns.doc('create_container')
    @ns.expect(container)
    def post(self):
        '''Create a Container'''
        return api.payload, 201

@ns.route('/<int:id>')
@ns.response(404, 'Container not found')
@ns.param('id', 'Container ID')
class Container(Resource):
    '''Funtions to set the status of a container'''
    @ns.doc('containerAddedToInventory')
    def post(self, id):
        '''Returns if the container is on a ship or not.'''
        return {'status':'false'}

    @ns.doc('delete_container')
    @ns.response(204, 'container deleted')
    def delete(self, id):
        '''Delete a task given its identifier'''
        return '', 204

    @ns.expect(container)
    def get(self, id):
        '''Update a task given its identifier'''
        return  api.payload

class containerActions(object):
    def __init__(self):
        self.counter = 0
        self.todos = []

    def ContainerAddedToInventory(self, id):
        for todo in self.todos:
            if todo['id'] == id:
                return todo
        api.abort(404, "Todo {} doesn't exist".format(id))

    def ContainerRemovedFromInventory(self, data):
        todo = data
        todo['id'] = self.counter = self.counter + 1
        self.todos.append(todo)
        return todo

    def ContainerAtLocation(self, id, data):
        todo = self.get(id)
        todo.update(data)
        return todo

    def ContainerOnMaintence(self, id):
        todo = self.get(id)
        self.todos.remove(todo)

    def ContainerOffMaintence(self, id):
        todo = self.get(id)
        self.todos.remove(todo)

    def ConatinerAssignedToOrder(self, id):
        todo = self.get(id)
        self.todos.remove(todo)

    def ContainerReleasedFromOrder(self, id):
        todo = self.get(id)
        self.todos.remove(todo)

    def ContainerGoodLoaded(self, id):
        todo = self.get(id)
        self.todos.remove(todo)

    def ContainerGoodUnLoaded(self, id):
        todo = self.get(id)
        self.todos.remove(todo)

    def ContainerOnShip(self, id):
        todo = self.get(id)
        self.todos.remove(todo)

    def ContainerOffShip(self, id):
        todo = self.get(id)
        self.todos.remove(todo)

    def ContainerOnTruck(self, id):
        todo = self.get(id)
        self.todos.remove(todo)

    def ContainerOffTruck(self, id):
        todo = self.get(id)
        self.todos.remove(todo)

@nsg.route('/healthcheck')
class generalChecks(Resource):
    '''Basic Application Checks'''
    @nsg.doc('healthcheck')
    def get(self):
        '''Runs a basic health check'''
        return {'status': 'UP'}, 200

if __name__ == '__main__':
    app.run(debug=True)
