void setup() {
    pinMode(3, INPUT_PULLUP); // INPUT_PULLUP no necesita resistencia 
    pinMode(13, OUTPUT);
}
void loop() {
    int sensorValue = digitalRead(3);
  
    if(sensorValue)
      {
        digitalWrite(13, HIGH); // Enciende el LED
      }
    else
      {
        digitalWrite(13, LOW); // Apaga el LED
      }
}
