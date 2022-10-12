/*
Este Programa es para probar la funcionalidad de un potenciometro
utilizando salida y entrada analoga.

By Alberto Marun
*/

//Declaraciones de Variables
//const int led = 3;
const int pot = A1;
int brillo;

void setup(){
Serial.begin(9600);
  
//pinMode(led, OUTPUT); // Declaramos el led como salida
// Los Pines analogicos se declaran de forma predeterminada como entrada
}

void loop()
{
// Aqui leemos el potenciometro y lo dividimos entre 4 
//ya que su maximo valor es 1023 y debemos llegar a valores de 255 
//por la maxima que soporta el analogWrite del Led

// Aqui escribimos el valor en el pin 3 donde esta el Led 
//analogWrite(led, brillo);

 
delay(500);  
// En el Serial Monitor pueden ver el valor que le da el Potenciometro
  Serial.println (analogRead(pot)); 
}
