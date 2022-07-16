import requests
import math
from src import app
from src.forms import Search_form
from functools import wraps
from flask import render_template,redirect,url_for, session, flash,Flask, jsonify,request
from werkzeug.exceptions import default_exceptions, HTTPException, InternalServerError
import json

@app.route("/", methods=['GET','POST'])
def index():
    search_form = Search_form()
    if search_form.validate_on_submit():
        if request.method == 'POST':
            return redirect(url_for("search", q = search_form.Key.data, page = 1))
           
    return render_template("index.html", search_form=search_form)


@app.route("/search", methods=['GET', 'POST'])
def search():
    search_form = Search_form()
    if request.method == 'POST':
        if search_form.validate_on_submit():
            return redirect(url_for("search", q = search_form.Key.data, page = 1))

    # q is the search_phrase passed as a query argument.
    # page is the page number passed as a query argument.
    search_phrase = request.args["q"]
    page = request.args["page"]

    # preparing the request to send to the server.
    headers = {'Content-Type': 'application/json'}
    response = requests.post(url = "http://localhost:8080", json={"search": search_phrase, "page": page})
    
    # getting the response and converting it to json.
    responseJson = response.json()

    # accessing the result (list of links with their titles and their descriptions)
    # accessing the numebr => overall number of results
    listOfDictsRes = responseJson["result"]
    numberOfResults = responseJson["number"]
    
    # rendering the template, and passing the arguments to it, including the current page number, the overall number of pages, and the search phrase.
    return render_template("result.jinja", search_form=search_form, searchPhrase = search_phrase, result_list = listOfDictsRes, page = page, numberOfResults = numberOfResults, numberOfPages = math.ceil(responseJson["number"] / 10.0))
    
#==================================================================================================================
def errorhandler(e):
    """Handle error"""
    if not isinstance(e, HTTPException):
        e = InternalServerError()
    return f"{e.name}, {e.code}"

# Listen for errors
for code in default_exceptions:
    app.errorhandler(code)(errorhandler)
