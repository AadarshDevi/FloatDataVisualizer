
const unsigned int baudRate = 9600;
const String startDataFlag = "--start-data-transfer";
const String endDataFlag = "--end-data-transfer";


void setup() {
  Serial.begin(baudRate);
  randomSeed(analogRead(A0));
  Serial.println(startDataFlag);
}

int i = 0;
int packet_id = 0;

void loop() {
  float time_in_s = micros() / 1000000.0; // s
  long depth = random(0, 500);
  long pressure = random(100, 1000);
  if(i >= 50) {
    Serial.println(endDataFlag);
    Serial.flush();
    Serial.end();
    while (true) {
      delay(1000);
    }
  }
  if(i%20 == 0) {
    packet_id++;
  }
  Serial.print("PN12-MiramarWaterJets,pkt-");
  Serial.print(packet_id);
  Serial.print(",");
  Serial.print(time_in_s);
  Serial.print(",");
  Serial.print(depth);
  Serial.print(",");
  Serial.println(pressure);
  i++;
  delay(500);
}
