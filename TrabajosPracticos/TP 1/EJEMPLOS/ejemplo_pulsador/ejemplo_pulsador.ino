const int led =13; // indica que el led está conectado al pin 2
const int bpulsar =12;
int boton;  // // se declara la variable boton
void setup() { 
       pinMode(bpulsar,INPUT); // se declara el pin 12 como entrada
       pinMode(led, OUTPUT);   // se declara el pin 2 como salida
} 
void loop() {
       boton=digitalRead(bpulsar);  //se asigna a la variable “boton” el valor del pin 12
       digitalWrite(led,boton);     // // el led se enciende o no de acuerdo al valor del push button 
}
