import string
from SearchE import app
from SearchE.forms import Search_form
from functools import wraps
from flask import render_template,redirect,url_for, session, flash,Flask, jsonify,request
from werkzeug.exceptions import default_exceptions, HTTPException, InternalServerError
import json

@app.route("/", methods=['GET','POST'])
def index():
    search_form = Search_form()
    if search_form.validate_on_submit():
        if request.method == 'POST':
            text_file = open("D:\GITHUB\Search-Engine\query.txt", "w")
            text_file.write(search_form.Key.data)
            text_file.close()
            return jsonify({'search':search_form.Key.data})
            #open text file
            
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
