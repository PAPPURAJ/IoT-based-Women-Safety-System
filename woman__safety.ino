#include "FirebaseESP8266.h"
#include <ESP8266WiFi.h>

#define FIREBASE_HOST "womensafety-3c8c4-default-rtdb.asia-southeast1.firebasedatabase.app"  
#define FIREBASE_AUTH "wuf9uMLtYgO2djjjI0Fwlv5mdvyXBWBEeD1PTDHr"

#define WIFI_SSID "IOT@Women"     
#define WIFI_PASSWORD "12341234" 

#include <TinyGPS++.h>

FirebaseData firebaseData;
FirebaseJson json;

double Lat=10,Lon=11;
TinyGPSPlus gps;



void writeDB(String field,String value){
 Firebase.setString(firebaseData, "/Location/"+field,value );
  
}







void setup() {
  
  pinMode(2,OUTPUT);
  Serial.begin(9600);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");
  
  while (WiFi.status() != WL_CONNECTED)
  {
  digitalWrite(2,0);
    Serial.print(".");
    delay(200);
    digitalWrite(2,1);
    delay(200);
  }
  
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.reconnectWiFi(true);
  writeDB("Test","Ok");

}

void loop() {
  
   while(Serial.available())              //While there are incoming characters  from the GPS
  {
    gps.encode(Serial.read());           //This feeds the serial NMEA data into the library one char at a time
  }
  if(gps.location.isUpdated())          //This will pretty much be fired all the time anyway but will at least reduce it to only after a package of NMEA data comes in
  {
    digitalWrite(2,0);
   
    Lat=gps.location.lat();
    Lon=gps.location.lng();
    writeDB("Lat",String(Lat,6));
    writeDB("Lon",String(Lon,6));
    Serial.println(Lat,6);
    Serial.println(Lon,6);
    delay(100);
    digitalWrite(2,1);

  }

}
