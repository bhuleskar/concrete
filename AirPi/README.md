AirPi
========

A Raspberry Pi weather station and air quality monitor.

This code was extended from the project located at http://airpi.es

Currently it is split into Upload.py, which handles getting all the data together and uploading it to the internet, and a variety of smaller peices of code for getting the data from each of the many sensors attached to the Pi.

Some of the files are based off code for the Raspberry Pi written by Adafruit: https://github.com/adafruit/Adafruit-Raspberry-Pi-Python-Code

An extension to it was a software component which used REST webservice to submit data to the Composite Information Server (CIS).

CIS was programmed to generate alerts and notify of any event which is configurable from the /events/EventCondition.csv file.



Steps to run:
$ python AirPi/src/Meteoros/Upload.py


Minimum Requirements:
	Python 1.7
	Account with Xively
	Configure AirPi.cfg file with API and Feed Id.
	This code uses CIS. Even without it, it would still display on the screen and send information to Xively if connected.
	