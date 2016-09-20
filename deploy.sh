export DIR=/data/local/tmp/play
export IP_ADDR=$(adb shell ip addr show wlan0 | grep " inet " | awk '{print $2}' | grep -o '^[^/]*')
export PORT=8080
export DEPLOY=./
adb shell rm -rf  $DIR
adb push $DEPLOY $DIR
echo "deployed $DEPLOY to $DIR, running Huston on http://$IP_ADDR:$PORT ..."
# get ipaddress
#adb shell ip -f inet addr show
#adb shell ip addr show wlan0 | grep " inet " | awk '{print $2}' | grep -o '^[^/]*'
# open browser
open http://$IP_ADDR:$PORT
# start the server
adb shell am start -a android.intent.action.VIEW -d "http://localhost:$PORT"
adb shell "(cd $DIR && ./run $PORT)"
