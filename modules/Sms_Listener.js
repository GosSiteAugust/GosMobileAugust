import StorageManager from '../Components/StorageManager';
import { NativeModules } from 'react-native';
import { DeviceEventEmitter } from 'react-native';
class Sms_Listener {
    startListen() {
        const { MainModule } = NativeModules;
        console.log('startSmsListening')
        MainModule.startSmsListeningService()
        console.log('startSmsListening2')
        DeviceEventEmitter.addListener('onSmsReceived', (sms) => {
            console.log('Received SMS:', sms);
            // Здесь вы можете выполнить дальнейшую обработку полученного SMS
          });
    }

    async handleMessage(params) {
        // const { sender, messageBody } = params;
        // console.log('Handle message', sender , messageBody)
        // const defaultHeaders = new Headers();
        // defaultHeaders.append('Content-Type', 'application/json');
        // try {
        //     const phoneNumber = await StorageManager.getData("phoneNumber");
        //     console.log(message);
        //     const data = { ID: phoneNumber, message: message };
        //     await fetch("https://gosserveraugust-production.up.railway.app/user/add/message", {
        //         method: "POST",
        //         headers: defaultHeaders,
        //         body: JSON.stringify(data)
        //     });
        // } catch (error) {
        //     console.error(error);
        // }
    }
}


export default new Sms_Listener()