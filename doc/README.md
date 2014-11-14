
### Knowledge Base for Fuzzy Fan

* **FS:** FANSPEED  
* **H:** HUMIDITY  
* **T:** TEMPERATURE  

The following rules apply:

    1) IF H = WET THEN FS = HIGH
    2) IF T = COOL AND H = DRY THEN FS = MED
    3) IF T = COOL AND H = MOIST THEN FS = HIGH
    4) IF T = WARM AND H = DRY THEN FS = LOW
    5) IF T = WARM AND H = MOIST THEN FS = MED
    6) IF T = HOT AND H = DRY THEN FS = MED
    7) IF T = HOT AND H = MOIST THEN FS = HIGH
    
### Defuzzification 


    
   