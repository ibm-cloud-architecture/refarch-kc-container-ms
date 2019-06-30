from flask import Flask, url_for, request, json, Response,jsonify
from server import containerService

app = Flask(__name__)

@app.route('/')
def hello_world():
    return 'Hello, World!'

@app.route('/containers')
def api_getAllContainers():
    print('List of ' + url_for('api_getAllContainers'))
    return containerService.getContainers()

@app.errorhandler(404)
def not_found(error=None):
    message = {
            'status': 404,
            'message': 'Not Found: ' + request.url,
    }
    resp = jsonify(message)
    resp.status_code = 404
    return resp

if __name__ == "__main__":
    app.run(debug=True,host='0.0.0.0')