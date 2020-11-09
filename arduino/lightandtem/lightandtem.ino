#include "DHT.h"
#define DHTPIN 2  
#define DHTTYPE DHT22  
DHT dht(DHTPIN, DHTTYPE);
int light = 5;
int fan=3;
int lso;
int t=0;
int h=0;
int a=0;
int set=0;
char i;
char *buf;
int input;
int onoff=0;

void setup() {
  Serial.begin(9600);
  pinMode(fan,OUTPUT);
   pinMode(light,OUTPUT);

  dht.begin();
}

void dht22(){
  h = dht.readHumidity(); 
  t = dht.readTemperature();
  if (isnan(h) || isnan(t) ) {
    Serial.println("Failed to read from DHT sensor!");
    return;
  }
  Serial.print(t);
  Serial.print(" ");
  Serial.println(h);
}

int count(){
  while(1){
    input=Serial.readBytes(buf,1);
    if(input==1){
      a++;
      }
      else{
        return a;
        break;
        }
  }
}

void loop() {
  delay(100);  
  dht22();
  
  a=0;
  set=count();
 

  if(set==456){
      if(onoff==0){
        digitalWrite(light, HIGH);
        onoff=1;
      }
      else{
        digitalWrite(light,LOW);
        onoff=0;
      }
    }




}
