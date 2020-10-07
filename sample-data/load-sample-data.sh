#!/bin/bash

mongoimport --db jobs-db --collection jobs --drop --jsonArray --file /code/sample-data/sample-data.json