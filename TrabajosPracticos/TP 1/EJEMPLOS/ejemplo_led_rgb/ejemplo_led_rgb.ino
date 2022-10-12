int pinLedR = 12;  // pin Rojo del led RGB
int pinLedV = 10;  // pin Verde del led RGB
int pinLedA = 11;   // pin Azul del led RGB

int pausa = 1000;

void setup() {
  pinMode(pinLedR, OUTPUT);    // pone el pinLedR como output
  pinMode(pinLedV, OUTPUT);    // pone el pinLedV como output
  pinMode(pinLedA, OUTPUT);    // pone el pinLedA como output
}

void loop() {
  //  colores basicos:
  color(255, 0, 0);   // rojo
  delay(pausa);       // delay por pausa
  color(0, 255, 0);   // verde
  delay(pausa);       // delay por pausa
  color(0, 0, 255);   // azul
  delay(pausa);       // delay por pausa

  // colores mezclados:
  color(255, 255, 255); // blanco
  delay(pausa);       // delay por pausa
  color(255, 255, 0); // amarillo
  delay(pausa);       // delay por pausa
  color(255, 0, 255); // magenta
  delay(pausa);       // delay por pausa
  color(0, 255, 255); // cian
  delay(pausa);       // delay por pausa
  color(0, 0, 0);     // apagado
  delay(pausa);       // delay por pausa
}

// funcion para generar colores
void color (int rojo, int verde, int azul) {
  analogWrite(pinLedR, rojo);
  analogWrite(pinLedV, verde);
  analogWrite(pinLedA, azul);
}
