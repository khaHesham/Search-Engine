from SearchE import app
from SearchE.forms import Search_form
from functools import wraps
from flask import render_template,redirect,url_for, session, flash
from werkzeug.exceptions import default_exceptions, HTTPException, InternalServerError

@app.route("/", methods=['GET','POST'])
def index():
    search_form = Search_form()
    if search_form.validate_on_submit():
        return "success"
    return render_template("index.html", search_form=search_form)

#==================================================================================================================
def errorhandler(e):
    """Handle error"""
    if not isinstance(e, HTTPException):
        e = InternalServerError()
    return f"{e.name}, {e.code}"

# Listen for errors
for code in default_exceptions:
    app.errorhandler(code)(errorhandler)
