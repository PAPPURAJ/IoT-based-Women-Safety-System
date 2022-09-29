#include "FirebaseESP8266.h"
#include <ESP8266WiFi.h>
#include <TinyGPS++.h>

#define FIREBASE_HOST "womensafety-3c8c4-default-rtdb.asia-southeast1.firebasedatabase.app"  
#define FIREBASE_AUTH "wuf9uMLtYgO2djjjI0Fwlv5mdvyXBWBEeD1PTDHr"

#define WIFI_SSID "Women Safety"     
#define WIFI_PASSWORD "12345678" 


FirebaseData firebaseData;
FirebaseJson json;

double lattitude,longitude;
TinyGPSPlus gps;



void setup() {
  
  pinMode(0,INPUT_PULLUP);
  pinMode(2,OUTPUT);
  Serial.begin(9600);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  
  while (WiFi.status() != WL_CONNECTED)
  {
  digitalWrite(2,0);
    delay(200);
    digitalWrite(2,1);
    delay(200);

    Serial.println(analogRead(A0));
    
  }

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.reconnectWiFi(true);

}

void loop() {
  
   while(Serial.available())
  {
    gps.encode(Serial.read());  
  }
  if(gps.location.isUpdated())  
  {
    digitalWrite(2,0);
   
    lattitude=gps.location.lat();
    longitude=gps.location.lng();

    Firebase.setString(firebaseData, "/Location/Lat",String(lattitude,6) );
    delay(100);
    Firebase.setString(firebaseData, "/Location/Lon",String(longitude,6) );
    delay(100);
    digitalWrite(2,1);

  }

  Firebase.setString(firebaseData, "/Danger",String(!digitalRead(0)));
  delay(300);

}
