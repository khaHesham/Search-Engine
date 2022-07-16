from flask import Flask
from flask_session import Session
from tempfile import mkdtemp

app = Flask(__name__)
# Ensure templates are auto-reloaded
app.config["TEMPLATES_AUTO_RELOAD"] = True
app.config['SECRET_KEY']='a826c5d6cc640bb26065f027'


#Data Base Connection
# app.config['MYSQL_USER']='root'
# app.config['MYSQL_PASSWORD']='1234'
# app.config['MYSQL_HOST']='localhost'
# app.config['MYSQL_DB']='financeverse'
# app.config['MYSQL_CURSORCLASS']='DictCursor'
# db=MySQL(app)

# Configure session to use filesystem (instead of signed cookies)
app.config["SESSION_FILE_DIR"] = mkdtemp()
app.config["SESSION_PERMANENT"] = False
app.config["SESSION_TYPE"] = "filesystem"
Session(app)


# Ensure responses aren't cached
@app.after_request
def after_request(response):
    response.headers["Cache-Control"] = "no-cache, no-store, must-revalidate"
    response.headers["Expires"] = 0
    response.headers["Pragma"] = "no-cache"
    return response


# def ExecuteQuery(query):
#     cur = db.connection.cursor()
#     result = cur.execute(query)
#     db.connection.commit()
#     return result

# def SelectQuery(query):
#     cur = db.connection.cursor()
#     cur.execute(query)
#     return cur.fetchone()

# def SelectMultipleQuery(query):
#     cur = db.connection.cursor()
#     cur.execute(query)
#     return cur.fetchall()

from src import routes
