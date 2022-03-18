from email.policy import default
from flask_wtf import FlaskForm
from werkzeug.exceptions import Locked
from wtforms import StringField, PasswordField, SubmitField, IntegerField, FloatField, DateField, SelectField
from wtforms import validators
from wtforms.validators import Length, EqualTo, DataRequired,NumberRange

class Search_form(FlaskForm):
    Key = StringField(label="Key", validators=[DataRequired()])
    Reset = SubmitField(label="Reset")
    Search = SubmitField(label="Search")
    #Time = SelectField(label="Time", choices=[(0,"TIME"), (1,"Last Time"), (2,"Today"), (3,"This week"), (4,"This month"), (5,"This year")], validators=[DataRequired()])
