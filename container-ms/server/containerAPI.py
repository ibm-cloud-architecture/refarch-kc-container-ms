from flask import Flask, Blueprint
from flask_restplus import Api, Resource, fields
from werkzeug.contrib.fixers import ProxyFix

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
@ns.param('id', 'The task identifier')
class Container(Resource):
    '''Show a single container item and lets you delete them'''
    @ns.doc('get_container')
    def get(self, id):
        '''Fetch a given resource'''
        return {'Show '}

    @ns.doc('delete_container')
    @ns.response(204, 'container deleted')
    def delete(self, id):
        '''Delete a task given its identifier'''
        return '', 204

    @ns.expect(container)
    def put(self, id):
        '''Update a task given its identifier'''
        return  api.payload

@nsg.route('/healthcheck')
class generalChecks(Resource):
    '''Basic Application Checks'''
    @nsg.doc('healthcheck')
    def get(self):
        '''Runs a basic health check'''
        return 'OK', 200

if __name__ == '__main__':
    app.run(debug=True)
