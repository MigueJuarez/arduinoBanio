#include <Wire.h>
#include "rgb_lcd.h"

#include <SoftwareSerial.h>

rgb_lcd lcd;

const int colorR = 255;
const int colorG = 0;
const int colorB = 0;

const char COMANDO_ESTADO_OCUPADO = 'O';
const char COMANDO_ESTADO_LIBRE = 'L';
const char COMANDO_ESTADO_PENDIENTE_DE_LIMPIEZA = 'P';
const char COMANDO_ESTADO_EN_LIMPIEZA = 'E';

const char COMANDO_SOLICITUD_LIMPIEZA = 'S';
const char COMANDO_ENVIAR_ESTADO = 'E';
const char COMANDO_INICIAR_LIMPIEZA = 'I';
const char COMANDO_FINALIZAR_LIMPIEZA = 'F';

#define GAP 205

#define POTE A1
#define SWITCH 3
#define SOLICITAR_LIMPIEZA 13
#define COMENZAR_LIMPIEZA 2


#define COLORRED 11
#define COLORBLUE 12
#define COLORGREEN 10
#define RS 9
#define E 8
#define DB4 7
#define DB5 6
#define DB6 5 
#define DB7 4


#define CAMBIAR_COLOR_VERDE 0
#define CAMBIAR_COLOR_ROJO 1
#define CAMBIAR_COLOR_AZUL 2


#define TIEMPO_MENSAJE 1000


#define LIBRE 0
#define OCUPADO 1
#define PENDIENTE_DE_LIMPIEZA_LIBRE 2
#define PENDIENTE_DE_LIMPIEZA_OCUPADO 3
#define EN_LIMPIEZA 4


#define ENTRA_PERSONA 1
#define SALE_PERSONA 0
#define SOLICITUD_LIMPIEZA 2
#define COMENZAR_O_FINALIZAR_LIMPIEZA 3
#define CONTINUAR 4
#define FIN_TIEMPO_MENSAJE 5
#define VARIACION_NIVEL_DE_SUCIEDAD 6
#define ENVIAR_ESTADO 14


#define ACTIVADO 1
#define DESACTIVADO 0

#define VALOR_INICIAL -1

#define COLUMNA_MSJ_FILA1 5
#define COLUMNA_MSJ_FILA2 0
#define FILA1 0
#define FILA2 1

#define LOG

SoftwareSerial miBT(6, 7);

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

void modificarColorLed(int color)
{
  if(color == CAMBIAR_COLOR_VERDE)
    colorLed(0,0,255);
  else if(color == CAMBIAR_COLOR_ROJO)
  colorLed(255,0,0);
  else
    colorLed(0,255,0);
}


void colorLed(int red, int blue, int green)
{
    analogWrite(COLORRED,red);
    analogWrite(COLORBLUE,blue);
    analogWrite(COLORGREEN,green);
}

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

    return hayEvento;
}

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

  return hayEvento;
}

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

  return hayEvento;
}

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
  return hayEvento;
}

int leerBTH()
{
  char msgeBT = '0';
    
  if (miBT.available())
  {
    msgeBT = char(miBT.read());
    log(msgeBT);
  }

 hayEvento = 0;
  
  if (msgeBT == COMANDO_INICIAR_LIMPIEZA || msgeBT == COMANDO_FINALIZAR_LIMPIEZA)
  {
    tipoEvento = COMENZAR_O_FINALIZAR_LIMPIEZA;
    hayEvento = 1;
  }
  else if (msgeBT == COMANDO_ENVIAR_ESTADO)
  {
    tipoEvento = ENVIAR_ESTADO;
    hayEvento = 1;
  }
  
  return hayEvento;
}

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


void escribirDisplayLCD(int columna,int fila,String mensaje)
{
  lcd.setCursor(columna,fila);
  lcd.print(mensaje);
}

void configuracionDeValores()
{
    Serial.begin(9600);

    miBT.begin(9600);

    lcd.begin(16, 2);

    lcd.setRGB(colorR, colorG, colorB);

    lcd.print("DIRT: ---");     
  
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

void maquinaDeEstados()
{
  tomarEvento();

  switch(estadoActual)
  {
    case LIBRE:
          switch(tipoEvento)
          {
              case ENTRA_PERSONA:
                modificarColorLed(CAMBIAR_COLOR_ROJO);
                escribirDisplayLCD(COLUMNA_MSJ_FILA1,FILA1," ---       ");
                estadoActual = OCUPADO;
                miBT.write(COMANDO_ESTADO_OCUPADO);
                tipoEvento = CONTINUAR;
                break;
              case SOLICITUD_LIMPIEZA:
                escribirDisplayLCD(COLUMNA_MSJ_FILA2,FILA2,"SOL. ENVIADA   ");
                temporizador = ACTIVADO;
                tiempoDesde = millis();
                estadoActual = PENDIENTE_DE_LIMPIEZA_LIBRE;

                miBT.write(COMANDO_SOLICITUD_LIMPIEZA);
                
                break;
              case VARIACION_NIVEL_DE_SUCIEDAD:
                escribirDisplayLCD(COLUMNA_MSJ_FILA1,FILA1,nivel_suciedad[estadoAnteriorNivelDeSuciedad]);
                estadoActual = LIBRE;
                tipoEvento = CONTINUAR;
                break;
              case CONTINUAR:
                estadoActual = LIBRE;
                break;
              case ENVIAR_ESTADO:
                miBT.write(COMANDO_ESTADO_LIBRE);
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
                estadoActual = LIBRE;
                miBT.write(COMANDO_ESTADO_LIBRE);
                break;
              case CONTINUAR:
                estadoActual = OCUPADO;
                break;
              case ENVIAR_ESTADO:
                miBT.write(COMANDO_ESTADO_OCUPADO);
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
                estadoActual = PENDIENTE_DE_LIMPIEZA_OCUPADO;
                miBT.write(COMANDO_ESTADO_OCUPADO);
                tipoEvento = CONTINUAR;
                break;
              case COMENZAR_O_FINALIZAR_LIMPIEZA:
                modificarColorLed(CAMBIAR_COLOR_AZUL);
                estadoActual = EN_LIMPIEZA;
                tipoEvento = CONTINUAR;
                break;
              case SOLICITUD_LIMPIEZA:
                escribirDisplayLCD(COLUMNA_MSJ_FILA2,FILA2,"SOL. YA ENVIADA");
                temporizador = ACTIVADO;
                tiempoDesde = millis();
                estadoActual = PENDIENTE_DE_LIMPIEZA_LIBRE;
                tipoEvento = CONTINUAR;
                break;
              case FIN_TIEMPO_MENSAJE:
                temporizador = DESACTIVADO;
                escribirDisplayLCD(COLUMNA_MSJ_FILA2,FILA2,"               ");
                estadoActual = PENDIENTE_DE_LIMPIEZA_LIBRE;
                tipoEvento = CONTINUAR;
                break;
              case VARIACION_NIVEL_DE_SUCIEDAD:
                escribirDisplayLCD(COLUMNA_MSJ_FILA1,FILA1,nivel_suciedad[estadoAnteriorNivelDeSuciedad]);
                estadoActual = PENDIENTE_DE_LIMPIEZA_LIBRE;
                tipoEvento = CONTINUAR;
                break;
              case CONTINUAR:
                estadoActual = PENDIENTE_DE_LIMPIEZA_LIBRE;
                break;
              case ENVIAR_ESTADO:
                miBT.write(COMANDO_ESTADO_PENDIENTE_DE_LIMPIEZA);
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
                miBT.write(COMANDO_ESTADO_LIBRE);
                estadoActual = PENDIENTE_DE_LIMPIEZA_LIBRE;
                tipoEvento = CONTINUAR;
                break;
              case FIN_TIEMPO_MENSAJE:
                temporizador = DESACTIVADO;
                escribirDisplayLCD(COLUMNA_MSJ_FILA2,FILA2,"               ");
                estadoActual = PENDIENTE_DE_LIMPIEZA_OCUPADO;
                tipoEvento = CONTINUAR;
                break;
              case CONTINUAR:
                estadoActual = PENDIENTE_DE_LIMPIEZA_OCUPADO;
                break;
              case ENVIAR_ESTADO:
                miBT.write(COMANDO_ESTADO_OCUPADO);
                tipoEvento = CONTINUAR;
                break;
          }
      break;
    case EN_LIMPIEZA:
          switch(tipoEvento)
          {
              case COMENZAR_O_FINALIZAR_LIMPIEZA:
                modificarColorLed(CAMBIAR_COLOR_VERDE);
                estadoActual = LIBRE;
                miBT.write(COMANDO_ESTADO_LIBRE);
                tipoEvento = CONTINUAR;
                break;
              case VARIACION_NIVEL_DE_SUCIEDAD:
                escribirDisplayLCD(COLUMNA_MSJ_FILA1,FILA1,nivel_suciedad[estadoAnteriorNivelDeSuciedad]);
                estadoActual = EN_LIMPIEZA;
                tipoEvento = CONTINUAR;
                break;
              case CONTINUAR:
                estadoActual = EN_LIMPIEZA;
                break;
              case ENVIAR_ESTADO:
                miBT.write(COMANDO_ESTADO_EN_LIMPIEZA);
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
