from email.policy import default
from flask_wtf import FlaskForm
from wtforms import StringField, SubmitField
from wtforms.validators import DataRequired

class Search_form(FlaskForm):
    Key = StringField(label="Key", validators=[DataRequired()])
    searchBtn = SubmitField(label="search") 