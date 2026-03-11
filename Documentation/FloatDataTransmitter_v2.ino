void setup() {
  Serial.begin(115200);
  randomSeed(analogRead(A0));
  Serial.println("--start-data-transfer");
}
int i = 0;
int packet_id = 0;
void loop() {
  unsigned long time_in_us = micros(); // us
  float time_in_s = ((float)time_in_us) / 1000000; // s
  long depth = random(0, 100);
  if(i >= 50) {
    delay(1000);
    Serial.println("--end-data-transfer");
    Serial.end();
    delay(1000);
    return;
  }
  if(i%20 == 0) {
    packet_id++;
  }
  Serial.println("PN12-MiramarWaterJets,pkt-"+String(packet_id)+","+String(time_in_s)+","+String(depth));
  i++;
  delay(500);
}
