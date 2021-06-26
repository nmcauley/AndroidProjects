import requests
import json

# This program serves to practice with API calls using python (requests) as well as handling the data in JSON format
# the goal is to be able to either format and display the information in terminal or some other GUI. Otherwise this may
# be an Android project

try:
    f = open("/Users/cauleynolan/Desktop/APIKey/Nasa.txt")
    # obtain key
    KEY = f.read()
    response = requests.get("https://api.nasa.gov/insight_weather/?api_key=" + KEY + "&feedtype=json&ver=1.0")
except ConnectionError or OSError:
    print("Error found, sorry.")


# class for Sol (Mars day)
class Sol:
    def __init__(self, sol_num):
        self.sol_num = sol_num

    def toString(self):
        return str(self.sol_num)


# JSON library functions
# json.dumps() — Takes in a Python object, and converts (dumps) it to a string.
# json.loads() — Takes a JSON string, and converts (loads) it to a Python object.
print(json.dumps(response.json(), sort_keys=True, indent=4))

# obtain sol numbers and create them into new Sol objects
sols_available = [Sol(i) for i in response.json()["sol_keys"]]

# it appears that the Curiosity rover is no longer retrieving valid data (at the time of checking 06/25/21)

