# Baño Inteligente

<sup> 
En el presente trabajo se propone presentar una solución innovadora a la problemática que representa la higiene en instalaciones sanitarias de acceso masivo. Estableciendo, que disponer de condiciones adecuadas para el uso no solo se trata de una cuestión de experiencia de usuario, sino también de salud pública, por lo que se ideó un prototipo que incorpora la electrónica para mejorar la información disponible sobre el estado actual de limpieza de un baño y la gestión eficiente de solicitudes de aseo a los mismos, optimizando el empleo de recursos destinados a tal fin, ya sea en edificios o eventos multitudinarios.
</sup>

### Integrantes:
    Bobbio Federico  
    Camejo Luciano 
    Juarez Miguel 
    Ojeda Juan 
    Stermann Axel

#### Introducción
El proyecto consiste fundamentalmente en el diseño y construcción de un sistema embebido que, mediante la interacción de determinados sensores y actuadores junto a la implementación de la lógica de negocio correcta, permite monitorear y comunicar de forma fehaciente el estado de higiene de un baño al usuario final (personas físicas) como así también solicitar la asistencia del personal asignado a las tareas de limpieza.

En el código fuente se utiliza el patrón de diseño de máquina de estados.

Link de acceso a la versión simulada en la plataforma Tinkercad: https://www.tinkercad.com/things/cSQm86Yjjdw

#### Bibliografía
 - https://naylampmechatronics.com/blog/35_tutorial-lcd-con-i2c-controla-un-lcd-con-solodos-pines.html


## Enunciado

### Objetivo
El  objetivo  de  este  trabajo  práctico  es  ofrecer  la  oportunidad de aprender  conceptos teóricos y su aplicación práctica sobre sistemas embebidos aplicados a internet de las cosas (IoT) o Sistemas ciber físicos (CPS).
Este  trabajo  debe  seguir  un  conjunto  de  pautas,  estas  van  desde  el  diseño  e implementación del circuito electrónico, como así también el Informe del trabajo. Por lo tanto, a continuación, se describen los ítems que deben cumplirse en el trabajo.

#### Con respecto a la construcción del Circuito electrónico
  - Debe estar construido con el simulador “circuits” de Tinkercad.
  - Se debe implementar el sistema embebido usando la placa de desarrollo Arduino Uno del simulador.
  - Las conexiones de los cables estén orientadas en forma horizontal o vertical. Tal como está explicado en el apunte “Electrónica y Arduino en Thinkercad”.
  - Los colores de los cables del circuito sigan es estándar decolor (rojo: positivo, negro: masa). Por otra parte, los demás cables deben ser de distinto color, por cada sensor y actuador utilizado.
  - Se utilice la “Placa de pruebas” (protoboard) en forma correcta.
  - Debe usauna fuente regulable externa para alimentar el circuito. 
  
#### Con respecto a la codificación del Sistema Embebido
  - La simulación debe funcionar sin errores.
  - El sistema embebido debe tener un mínimo de lógica de procesamiento. Esto se logra  haciendo  que  los  sensores  interactúen  con  los  actuadores.  No  es  válido desarrollar un simple “interruptor”.
  - No   usar   funciones   bloqueantes   como   delay. Tips: Usar   el   concepto   de temporizadores explicados en el apunte “Electrónica y Arduino en Tinkercad”.
  - Debe estar implementado el patrón de diseño máquina de estados.-No usar números mágicos.
  - Las líneas de código deben estar documentadas lógicamente.
  - Debe  entregarse  una  versión  final.  No  debe  existir  código  comentado  o redundante. 

#### Con respecto a la calidaddel Informe
  - Entregar  el  informe por  plataforma  MIeL.  Este  debe  ser  en formato  .pdf,  con nombre TP1_DiaCursada_NumerodelGrupo.pdf.
  - Desarrollar  el  informe  en  formato  paper.  Que  contenga  las  secciones  de encabezado,   introducción,   desarrollo   y   bibliografía.   El   formato   del   paper solicitado se muestra en el siguiente enlace:
    https://www.dropbox.com/s/2d7whc9sxi2o8ml/00_EstructuraPaper_cacicTP1.doc?dl=0


###### Sistemas Operativos Avanzados - 2° Cuatrimestre de 2022
