 /*
VTU Final year project
C Gautham Raju     1NH09CS018
Gautam Doulani     1NH09CS030
Jonathan Sudhakar  1NH09CS036
Mohit Nahak        1NH09CS054
 */

#include <SPI.h>
#include <WiFi.h>
#include <SoftwareSerial.h>
#include <TinyGPS.h>

char ssid[] = "dalibor";     //  your network SSID (name) 
char pass[] = "123456789";   // your network password
int keyIndex = 0;            // your network key Index number (needed only for WEP)
char response[1000];
int flag=0;
int f=0,b=0;
int IN1=30;
int IN2=31;
int IN3=32;
int IN4=33;

int IN5=34;
int IN6=35;
int IN7=36;
int IN8=37;

int led=45;
int proxBack=49;
int proxFront=48;

int blinkerRight=13;
int blinkerLeft=14;

float flat=0.0, flon=0.0;
float temp=0.0;
unsigned long age;

TinyGPS gps;
SoftwareSerial ss(15, 14);
 
int status = WL_IDLE_STATUS;

// Initialize the Wifi client library
WiFiClient client;

// server address:
char server[] = "192.168.1.10";
//IPAddress server(64,131,82,241);

unsigned long lastConnectionTime = 0;         // last time you connected to the server, in milliseconds
boolean lastConnected = false;                // state of the connection last time through the main loop
const unsigned long postingInterval = 1*300;  // delay between updates, in milliseconds

void setup() {
  //Initialize serial and wait for port to open:
  Serial.begin(9600); 
  ss.begin(9600);
  pinMode(led,OUTPUT);
  digitalWrite(led,LOW);
  while (!Serial) {
    ; // wait for serial port to connect. Needed for Leonardo only
  }
  
  // check for the presence of the shield:
  if (WiFi.status() == WL_NO_SHIELD) {
    Serial.println("WiFi shield not present"); 
    // don't continue:
    while(true);
  } 
  
  // attempt to connect to Wifi network:
  while ( status != WL_CONNECTED) { 
    Serial.print("Attempting to connect to SSID: ");
    Serial.println(ssid);
    // Connect to WPA/WPA2 network. Change this line if using open or WEP network:    
    status = WiFi.begin(ssid, pass);

    // wait 10 seconds for connection:
    delay(3000);
  } 
  // you're connected now, so print out the status:
  printWifiStatus();
  pinMode(IN1,OUTPUT);
  pinMode(IN2,OUTPUT);
  pinMode(IN3,OUTPUT);
  pinMode(IN4,OUTPUT);
  
  pinMode(IN5,OUTPUT);
  pinMode(IN6,OUTPUT);
  pinMode(IN7,OUTPUT);
  pinMode(IN8,OUTPUT);
  
  pinMode(proxFront,INPUT);
  pinMode(proxBack,INPUT);
  
  pinMode(blinkerLeft,OUTPUT);
  pinMode(blinkerRight,OUTPUT);

}

void loop() { 
  
  int i=0;
  // if there's incoming data from the net connection.  
 
  bool newData = false;
  
  for (unsigned long start = millis(); millis() - start < 1000;)
  {
    while (ss.available())
    {
      char c = ss.read();
      // Serial.write(c); // uncomment this line if you want to see the GPS data flowing
      if (gps.encode(c)) // Did a new valid sentence come in?
        newData = true;
    }
  }
   
  gps.f_get_position(&flat, &flon, &age);
 
  while (client.available()) {
    response[i]=client.read();
    i++;
  }

  // if there's no net connection, but there was one last time
  // through the loop, then stop the client:
  if (!client.connected() && lastConnected) {
    Serial.println();
    Serial.println("disconnecting.");
    f=0;
    b=0;
    proximity();
    find();
    client.stop();
  }

  // if you're not connected, and ten seconds have passed since
  // your last connection, then connect again and send data:
  if(!client.connected() && (millis() - lastConnectionTime > postingInterval)) {
    httpRequest();
    flag=1;
  }
  
  // store the state of the connection for next time through
  // the loop:
  lastConnected = client.connected();
}

// this method makes a HTTP connection to the server:
void httpRequest() {
  // if there's a successful connection:
  if (client.connect(server, 80)) 
  {
    Serial.println("connecting...");
    
    /**
    *Coverting position to integer. Divide by 100000 in server
    **/
    long iLat=(long)(flat*100000);
    long iLon=(long)(flon*100000);
    
    String sLat=String(iLat);
    String sLon=String(iLon);
    
    client.println("GET /webapi/public/poll/dalibor?lat="+sLat+"&lon="+sLon+" HTTP/1.1");
    //client.println(str);
    client.println("Host: localhost");
    client.println("User-Agent: arduino-ethernet");
    client.println("Connection: close");
    client.println();

    // note the time that the connection was made:
    lastConnectionTime = millis();
  } 
  else 
  {
    // if you couldn't make a connection:
    Serial.println("connection failed");
    Serial.println("disconnecting.");
    client.stop();
  }
}

void printWifiStatus() {
  // print the SSID of the network you're attached to:
  Serial.print("SSID: ");
  Serial.println(WiFi.SSID());

  // print your WiFi shield's IP address:
  IPAddress ip = WiFi.localIP();
  Serial.print("IP Address: ");
  Serial.println(ip);

  // print the received signal strength:
  long rssi = WiFi.RSSI();
  Serial.print("signal strength (RSSI):");
  Serial.print(rssi);
  Serial.println(" dBm");
}

void find()
{
  char* ptr;
  ptr=strstr(response,"Move");
  char cmd[5];
 
  cmd[0]=*(ptr+6);
  cmd[1]=*(ptr+7);
  cmd[2]=*(ptr+8);
  cmd[3]=*(ptr+9);
  cmd[4]='\0';
  //Serial.write(cmd);
   String cmdStr=cmd;
   digitalWrite(led,HIGH);
   switch(cmdStr.toInt())
   {
     case 1000:
     Serial.write("straight");
     if(f!=1)
       {
           straight(); 
       }
     break;
     case 1100:
     Serial.write("back");
     if(b!=1)
       {
           backward(); 
       }
     break;
     case 1010:straight_left();
     Serial.write("straight_left");
     break;
     case 1001:straight_right();
     Serial.write("straight_right");
     break;
     case 1110:backward_left();
     Serial.write("back_left");
     break;
     case 1101:backward_right();
     Serial.write("back_right");
     break;
     default:
     Serial.write("brake");
     brake();
   }
 }
 
 void straight()
 {
    digitalWrite(IN1,HIGH);
    digitalWrite(IN2,LOW);
    digitalWrite(IN3,HIGH);
    digitalWrite(IN4,LOW);
    
    digitalWrite(IN5,HIGH);
    digitalWrite(IN6,LOW);
    digitalWrite(IN7,HIGH);
    digitalWrite(IN8,LOW);
    
    blinkStop();
 }
 void backward()
 {
    digitalWrite(IN1,LOW);
    digitalWrite(IN2,HIGH);
    digitalWrite(IN3,LOW);
    digitalWrite(IN4,HIGH);
    
    digitalWrite(IN5,LOW);
    digitalWrite(IN6,HIGH);
    digitalWrite(IN7,LOW);
    digitalWrite(IN8,HIGH);
    
    blinkStop();
 }
 void straight_left()
 {
    digitalWrite(IN1,HIGH);
    digitalWrite(IN2,LOW);
    digitalWrite(IN3,LOW);
    digitalWrite(IN4,LOW);
    
    digitalWrite(IN5,HIGH);
    digitalWrite(IN6,LOW);
    digitalWrite(IN7,LOW);
    digitalWrite(IN8,LOW);
    
    blinkLeft();
 }
 void straight_right()
 {
    digitalWrite(IN1,LOW);
    digitalWrite(IN2,LOW);
    digitalWrite(IN3,HIGH);
    digitalWrite(IN4,LOW);
    
    digitalWrite(IN5,LOW);
    digitalWrite(IN6,LOW);
    digitalWrite(IN7,HIGH);
    digitalWrite(IN8,LOW);
    
    blinkRight();
 }
 void backward_left()
 {
    digitalWrite(IN1,LOW);
    digitalWrite(IN2,LOW);
    digitalWrite(IN3,LOW);
    digitalWrite(IN4,HIGH);
    
    digitalWrite(IN5,LOW);
    digitalWrite(IN6,LOW);
    digitalWrite(IN7,LOW);
    digitalWrite(IN8,HIGH);
    
    blinkLeft();
 }
 void backward_right()
 {
    digitalWrite(IN1,LOW);
    digitalWrite(IN2,HIGH);
    digitalWrite(IN3,LOW);
    digitalWrite(IN4,LOW);
    
    digitalWrite(IN5,LOW);
    digitalWrite(IN6,HIGH);
    digitalWrite(IN7,LOW);
    digitalWrite(IN8,LOW);
    
    blinkRight();
 }
 void brake()
 {
    digitalWrite(IN1,LOW);
    digitalWrite(IN2,LOW);
    digitalWrite(IN3,LOW);
    digitalWrite(IN4,LOW);
    
    digitalWrite(IN5,LOW);
    digitalWrite(IN6,LOW);
    digitalWrite(IN7,LOW);
    digitalWrite(IN8,LOW);
    
    blinkStop();
 }
 
 void proximity()
 {
   if(digitalRead(proxFront))
   {
     brake();
     f=1;
   }
   
   if(digitalRead(proxBack))
   {
     brake();
     b=1;
   }
 }
 
 void blinkLeft()
 {
   digitalWrite(blinkerLeft,HIGH);
   digitalWrite(blinkerRight,LOW);   
 }
 
 void blinkRight()
 {
   digitalWrite(blinkerLeft,LOW);
   digitalWrite(blinkerRight,HIGH);
 }
 
 void blinkStop()
 {
   digitalWrite(blinkerLeft,LOW);
   digitalWrite(blinkerRight,LOW);
 }
