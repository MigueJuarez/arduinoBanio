#include <Wire.h>
#include "rgb_lcd.h"

#include <SoftwareSerial.h> // libreria que permite establecer pines digitales
                            // para comunicacion serie

rgb_lcd lcd;

const int colorR = 255;
const int colorG = 0;
const int colorB = 0;


// indica cada cuanto cambia el nivel de suciedad 
#define GAP 205

// pines de los sensores
#define POTE A1
#define SWITCH 3
#define SOLICITAR_LIMPIEZA 13
#define COMENZAR_LIMPIEZA 2
#define ENVIAR_ESTADO 14

//pines de los actuadores
#define COLORRED 11
#define COLORBLUE 12
#define COLORGREEN 10
#define RS 9
#define E 8
#define DB4 7
#define DB5 6
#define DB6 5 
#define DB7 4

//macros para indicar al color que quiero cambiar 
#define CAMBIAR_COLOR_VERDE 0
#define CAMBIAR_COLOR_ROJO 1
#define CAMBIAR_COLOR_AZUL 2

//tiempo de visualizacion de mensaje en display LCD
#define TIEMPO_MENSAJE 1000

//estados posibles
#define LIBRE 0
#define OCUPADO 1
#define PENDIENTE_DE_LIMPIEZA_LIBRE 2
#define PENDIENTE_DE_LIMPIEZA_OCUPADO 3
#define EN_LIMPIEZA 4

//eventos posibles
#define ENTRA_PERSONA 1
#define SALE_PERSONA 0
#define SOLICITUD_LIMPIEZA 2
#define COMENZAR_O_FINALIZAR_LIMPIEZA 3
#define CONTINUAR 4
#define FIN_TIEMPO_MENSAJE 5
#define VARIACION_NIVEL_DE_SUCIEDAD 6

/* estados posibles para el temporizador del mensaje 
 "sol. enviada" o "sol. ya enviada" del display LCD */
#define ACTIVADO 1
#define DESACTIVADO 0

#define VALOR_INICIAL -1

//indican sobre qué posicion se  desea escribir en el display LCD
#define COLUMNA_MSJ_FILA1 5
#define COLUMNA_MSJ_FILA2 0
#define FILA1 0
#define FILA2 1

#define LOG

SoftwareSerial miBT(6, 7);  // pin 6 como RX, pin 7 como TX

String nivel_suciedad[] = {{"IMPECABLE  "},{"LIMPIO     "},{"MODERADO   "},{"SUCIO      "},{"MUY SUCIO  "}};

int hayEvento = 0;
int estadoActual;
int tipoEvento;

int estadoAnteriorSwitch;
int estadoAnteriorPulsadorSolicitarLimpieza = LOW;
int estadoAnteriorPulsadorComenzarLimpieza = LOW;
int estadoAnteriorNivelDeSuciedad;
int estadoActualNivelDeSuciedad;


int temporizador = DESACTIVADO;
int tiempoDesde;
int tiempoHasta;

void log(String estado, String evento)
{
#ifdef LOG
    Serial.println("------------------------------------------------");
    Serial.println(estado);
    Serial.println(evento);
    Serial.println("------------------------------------------------");
#endif
}

void log(String msj)
{
  #ifdef LOG
  Serial.println(msj);
  #endif
}

void log(int val)
{
  #ifdef LOG
 Serial.println(val); 
  #endif
}

/*consiste en modificar el color del led a algún color valido
es decir, ROJO o VERDE o AZUL*/
void modificarColorLed(int color)
{
  if(color == CAMBIAR_COLOR_VERDE)
    colorLed(0,0,255);
  else if(color == CAMBIAR_COLOR_ROJO)
  colorLed(255,0,0);
  else
    colorLed(0,255,0);
}

/*esta funcion consiste en enviar  un valor entre 0 y 255 
para indicar en color del led deseado*/
void colorLed(int red, int blue, int green)
{
    analogWrite(COLORRED,red);
    analogWrite(COLORBLUE,blue);
    analogWrite(COLORGREEN,green);
}

/*consiste en realizar la lectura del potenciometro
para verificar si se generó un evento*/
int verificarEventoPotenciometro()
{
  hayEvento = 0;
  int valPote = analogRead(POTE);
    estadoActualNivelDeSuciedad = valPote / GAP;
    if(estadoAnteriorNivelDeSuciedad != estadoActualNivelDeSuciedad)
    {
        tipoEvento = VARIACION_NIVEL_DE_SUCIEDAD;
        estadoAnteriorNivelDeSuciedad = estadoActualNivelDeSuciedad;
      hayEvento = 1;
    }
   // log("verificando potenciometro");
   // log(estadoActualNivelDeSuciedad);
    return hayEvento;
}

/*consiste en realizar la lectura del interruptor 
deslizante para verificar si se generó un evento*/
int verificarEventoSensorSwitch()
{ 
  hayEvento = 0;
  int estadoSensor = digitalRead(SWITCH);
  if(estadoSensor != estadoAnteriorSwitch)
  {
    estadoAnteriorSwitch = estadoSensor;
    tipoEvento = estadoSensor;
    hayEvento = 1; 
  }
  //log("verificando switch");
  //log(estadoSensor);
  return hayEvento;
}

/*consiste en realizar la lectura del pulsador para verificar
si se generó un evento*/
int verficarEventoPulsadorSolicitudDeLimpieza()
{
  hayEvento = 0; 
  if(temporizador == ACTIVADO)
  return hayEvento;
  int estadoSensor = digitalRead(SOLICITAR_LIMPIEZA);
  
  if(estadoSensor == HIGH && estadoAnteriorPulsadorSolicitarLimpieza == LOW)
  {
    tipoEvento = SOLICITUD_LIMPIEZA;
    hayEvento = 1;
  }
  estadoAnteriorPulsadorSolicitarLimpieza = estadoSensor;
    //log("verificando pulsador de solicitud de limpieza");
    //log(estadoSensor);
  return hayEvento;
}

/*consiste en realizar la lectura del pulsador para verificar
si se generó un evento*/
int verificarEventoPulsadorDeLimpieza()
{
  hayEvento = 0;
  int estadoSensor = digitalRead(COMENZAR_LIMPIEZA);
  if(estadoSensor == HIGH && estadoAnteriorPulsadorComenzarLimpieza == LOW)
  {
    tipoEvento = COMENZAR_O_FINALIZAR_LIMPIEZA;
    hayEvento = 1;
  }
  estadoAnteriorPulsadorComenzarLimpieza = estadoSensor;
  //log("verificando pulsador de iniciar limpieza");
   // log(estadoSensor);
    return hayEvento;
}

/*
int leerBTH()
{
  char msgeBT;
    
  if (miBT.available())
  {
    msgeBT = char(miBT.read());
    log("Llego bien la letra ");
    log((char)msgeBT);

    
  }
  
  hayEvento = (enviarEstadoActual(msgeBT));
  hayEvento = (verificarEventoBTELimpieza(msgeBT));
  
  return hayEvento;
}*/


int leerBTH()
{
  char msgeBT = '0';
    
  if (miBT.available())       // si hay informacion disponible desde modulo
  {
    msgeBT = char(miBT.read());
    log("Llego bien la letra ");
    log(msgeBT);
  }

 hayEvento = 0;
  
  if (msgeBT == 'I' || msgeBT == 'F')
  {
    //log("Me llego una i o una f");
    tipoEvento = COMENZAR_O_FINALIZAR_LIMPIEZA;
    hayEvento = 1;
  }
  else if (msgeBT == 'E')
  {
    tipoEvento = ENVIAR_ESTADO;
    hayEvento = 1;
    //log("Me llego una e");
  }
  
  return hayEvento;
}


/*consiste en realizar la lectura del Modulo BTE para verificar
si se generó un evento*/
/*int verificarEventoBTELimpieza(char msgeBT)
{
  hayEvento = 0;
  
  if (msgeBT == 'I' || msgeBT == 'F')
  {
    tipoEvento = COMENZAR_O_FINALIZAR_LIMPIEZA;
    hayEvento = 1;
    log("Me llego una i o una f");
  }
  
  return hayEvento;
}*/

/*esta funcion envia el estado actual del baño*/
/*int enviarEstadoActual(char msgeBT)
{
  hayEvento = 0;
  
  if (msgeBT == 'E')
  {
    tipoEvento = ENVIAR_ESTADO;
    hayEvento = 1;
    log("Me llego una e");
  }
  
  return hayEvento;
}*/

/*esta funcion toma los eventos provenientes de cada
uno de los sensores*/
void tomarEvento()
{
  if(temporizador)
  {
    tiempoHasta = millis();
    if(tiempoHasta - tiempoDesde > TIEMPO_MENSAJE)
    {
      tiempoDesde = tiempoHasta;
      tipoEvento = FIN_TIEMPO_MENSAJE;
      return;
    }    
  }

  if(leerBTH())
    return;
  
  if(verificarEventoSensorSwitch())
    return;
  if(verficarEventoPulsadorSolicitudDeLimpieza())
    return;
  if(verificarEventoPulsadorDeLimpieza())
    return;
  
  if(verificarEventoPotenciometro())
    return;

  tipoEvento = CONTINUAR;
}

/*esta funcion consiste en realizar una escritura de cierto
mensaje en el display LCD en la posicion indicada*/
void escribirDisplayLCD(int columna,int fila,String mensaje)
{
  lcd.setCursor(columna,fila);
  lcd.print(mensaje);
}

/*esta funcion consiste en incializar el display LCD, definir
los pines de entrada y salida y determinar el estado inicial
del sistema*/
void configuracionDeValores()
{
    Serial.begin(9600);
      //BLUETH
    miBT.begin(9600);    // comunicacion serie entre Arduino y el modulo a 9600 bps

// set up the LCD's number of columns and rows:
    lcd.begin(16, 2);

    lcd.setRGB(colorR, colorG, colorB);

    // Print a message to the LCD.
    lcd.print("DIRT: ---");

    delay(1000);      
  
    pinMode(SWITCH,INPUT);
    pinMode(SOLICITAR_LIMPIEZA,INPUT);
    pinMode(COMENZAR_LIMPIEZA, INPUT);
  
    pinMode(COLORRED,OUTPUT);
    pinMode(COLORBLUE,OUTPUT);
    pinMode(COLORGREEN,OUTPUT);
  
    estadoActual = digitalRead(SWITCH);
    estadoAnteriorSwitch = estadoActual;
    if(estadoActual == LIBRE)
      modificarColorLed(CAMBIAR_COLOR_VERDE);
    else
      modificarColorLed(CAMBIAR_COLOR_ROJO);
  
    estadoAnteriorNivelDeSuciedad = VALOR_INICIAL;
}

/*representa la maquina de estados donde se visualizan los 
estados, los eventos de cada estado y el impacto que genera
cada evento sobre el sistema*/
void maquinaDeEstados()
{
  tomarEvento();

  /*
  if (hayEvento)
  {
    log("Mostrar cambio evento : ");
    log(estadoActual);
  }
  
  if (hayEvento)
    log(tipoEvento);
    */
  switch(estadoActual)
  {
    case LIBRE:
          switch(tipoEvento)
          {
              case ENTRA_PERSONA:
                modificarColorLed(CAMBIAR_COLOR_ROJO);
                escribirDisplayLCD(COLUMNA_MSJ_FILA1,FILA1," ---       ");
                log("ESTADO_LIBRE","EVENTO_ENTRA_PERSONA");
                estadoActual = OCUPADO;
                miBT.write('O');
                tipoEvento = CONTINUAR;
                break;
              case SOLICITUD_LIMPIEZA:
                escribirDisplayLCD(COLUMNA_MSJ_FILA2,FILA2,"SOL. ENVIADA   ");
                temporizador = ACTIVADO;
                tiempoDesde = millis();
                log("ESTADO_LIBRE","EVENTO_SOLICITUD_LIMPIEZA");
                estadoActual = PENDIENTE_DE_LIMPIEZA_LIBRE;
                
                //BLUETH
                // if (miBT.available())     // si hay informacion disponible desde el miBT
                miBT.write('S');   // lee monitor serial y envia a Bluetooth
                
                break;
              case VARIACION_NIVEL_DE_SUCIEDAD:
                escribirDisplayLCD(COLUMNA_MSJ_FILA1,FILA1,nivel_suciedad[estadoAnteriorNivelDeSuciedad]);
                //log("ESTADO_LIBRE","EVENTO_VARIACION_NIVEL_DE_SUCIEDAD");
                estadoActual = LIBRE;
                tipoEvento = CONTINUAR;
                break;
              case CONTINUAR:
                //log("ESTADO_LIBRE","EVENTO_CONTINUAR");
                estadoActual = LIBRE;
                break;
              case ENVIAR_ESTADO:
                miBT.write('L');
                tipoEvento = CONTINUAR;
                break;
          }
          break;
    case OCUPADO:
          switch(tipoEvento)
          {
              case SALE_PERSONA:
                modificarColorLed(CAMBIAR_COLOR_VERDE);
                escribirDisplayLCD(COLUMNA_MSJ_FILA1,FILA1,nivel_suciedad[estadoAnteriorNivelDeSuciedad]);
                log("ESTADO_OCUPADO","EVENTO_SALE_PERSONA");
                estadoActual = LIBRE;
                miBT.write('L');
                break;
              case CONTINUAR:
                //log("ESTADO_OCUPADO","EVENTO_CONTINUAR");
                estadoActual = OCUPADO;
                break;
              case ENVIAR_ESTADO:
                miBT.write('O');
                tipoEvento = CONTINUAR;
                break;
          }
          break;
    case PENDIENTE_DE_LIMPIEZA_LIBRE:
          switch(tipoEvento)
          {
              case ENTRA_PERSONA:
                modificarColorLed(CAMBIAR_COLOR_ROJO);
                escribirDisplayLCD(COLUMNA_MSJ_FILA1,FILA1," ---       ");
                log("ESTADO_PENDIENTE_DE_LIMPIEZA_LIBRE","EVENTO_ENTRA_PERSONA");
                estadoActual = PENDIENTE_DE_LIMPIEZA_OCUPADO;
                miBT.write('O');
                tipoEvento = CONTINUAR;
                break;
              case COMENZAR_O_FINALIZAR_LIMPIEZA:
                modificarColorLed(CAMBIAR_COLOR_AZUL);
                log("ESTADO_PENDIENTE_DE_LIMPIEZA_LIBRE","EVENTO_COMENZAR_O_FINALIZAR_LIMPIEZA");
                estadoActual = EN_LIMPIEZA;
                tipoEvento = CONTINUAR;
                break;
              case SOLICITUD_LIMPIEZA:
                escribirDisplayLCD(COLUMNA_MSJ_FILA2,FILA2,"SOL. YA ENVIADA");
                temporizador = ACTIVADO;
                tiempoDesde = millis();
                log("ESTADO_PENDIENTE_DE_LIMPIEZA_LIBRE","EVENTO_SOLICITUD_LIMPIEZA");
                estadoActual = PENDIENTE_DE_LIMPIEZA_LIBRE;
                tipoEvento = CONTINUAR;
                break;
              case FIN_TIEMPO_MENSAJE:
                temporizador = DESACTIVADO;
                escribirDisplayLCD(COLUMNA_MSJ_FILA2,FILA2,"               ");
                log("ESTADO_PENDIENTE_DE_LIMPIEZA_LIBRE","EVENTO_FIN_TIEMPO_MENSAJE");
                estadoActual = PENDIENTE_DE_LIMPIEZA_LIBRE;
                tipoEvento = CONTINUAR;
                break;
              case VARIACION_NIVEL_DE_SUCIEDAD:
                escribirDisplayLCD(COLUMNA_MSJ_FILA1,FILA1,nivel_suciedad[estadoAnteriorNivelDeSuciedad]);
                //log("ESTADO_PENDIENTE_DE_LIMPIEZA_LIBRE","EVENTO_VARIACION_NIVEL_DE_SUCIEDAD");
                estadoActual = PENDIENTE_DE_LIMPIEZA_LIBRE;
                tipoEvento = CONTINUAR;
                break;
              case CONTINUAR:
                //log("ESTADO_PENDIENTE_DE_LIMPIEZA_LIBRE","EVENTO_CONTINUAR");
                estadoActual = PENDIENTE_DE_LIMPIEZA_LIBRE;
                break;
              case ENVIAR_ESTADO:
                miBT.write('P');
                tipoEvento = CONTINUAR;
                break;
          }
          break;
    case PENDIENTE_DE_LIMPIEZA_OCUPADO:
          switch(tipoEvento)
          {
              case SALE_PERSONA:
                modificarColorLed(CAMBIAR_COLOR_VERDE);
                escribirDisplayLCD(COLUMNA_MSJ_FILA1,FILA1,nivel_suciedad[estadoAnteriorNivelDeSuciedad]);
                log("ESTADO_PENDIENTE_DE_LIMPIEZA_OCUPADO","EVENTO_SALE_PERSONA");
                miBT.write('L');
                estadoActual = PENDIENTE_DE_LIMPIEZA_LIBRE;
                tipoEvento = CONTINUAR;
                break;
              case FIN_TIEMPO_MENSAJE:
                temporizador = DESACTIVADO;
                escribirDisplayLCD(COLUMNA_MSJ_FILA2,FILA2,"               ");
                log("ESTADO_PENDIENTE_DE_LIMPIEZA_OCUPADO","EVENTO_FIN_TIEMPO_MENSAJE");
                estadoActual = PENDIENTE_DE_LIMPIEZA_OCUPADO;
                tipoEvento = CONTINUAR;
                break;
              case CONTINUAR:
                //log("ESTADO_PENDIENTE_DE_LIMPIEZA_OCUPADO","EVENTO_CONTINUAR");
                estadoActual = PENDIENTE_DE_LIMPIEZA_OCUPADO;
                break;
              case ENVIAR_ESTADO:
                miBT.write('O');
                tipoEvento = CONTINUAR;
                break;
          }
      break;
    case EN_LIMPIEZA:
          switch(tipoEvento)
          {
              case COMENZAR_O_FINALIZAR_LIMPIEZA:
                modificarColorLed(CAMBIAR_COLOR_VERDE);
                log("ESTADO_EN_LIMPIEZA","EVENTO_CONTINUAR");
                estadoActual = LIBRE;
                miBT.write('L');
                tipoEvento = CONTINUAR;
                break;
              case VARIACION_NIVEL_DE_SUCIEDAD:
                escribirDisplayLCD(COLUMNA_MSJ_FILA1,FILA1,nivel_suciedad[estadoAnteriorNivelDeSuciedad]);
                log("ESTADO_EN_LIMPIEZA","EVENTO_VARIACION_NIVEL_DE_SUCIEDAD");
                estadoActual = EN_LIMPIEZA;
                tipoEvento = CONTINUAR;
                break;
              case CONTINUAR:
                //log("ESTADO_EN_LIMPIEZA","EVENTO_CONTINUAR");
                estadoActual = EN_LIMPIEZA;
                break;
              case ENVIAR_ESTADO:
                miBT.write('E');
                tipoEvento = CONTINUAR;
                break;                
          }
      break;
  }
}
  
void setup()
{
 configuracionDeValores();
}

void loop()
{
  maquinaDeEstados();
}
