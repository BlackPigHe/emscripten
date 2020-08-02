var err = 0
var opusDecode = Module._opus_decoder_create(48000,2,err);
console.log("创建的decode===="+opusDecode)
//inputData 为服务端传过来的pcm数据
var inputData = new Uint8Array(240)
// 申请一块内存，作为传入c接口的
var input_addr = Module._malloc(240);
Module.HEAPU8.set(inputData,inputData.length)
var output_addr = Module._malloc(1024);
// 一帧值设为1024
var frame_size = 1024
var opus_Decode_lenght = Module._opus_decode(opusDecode,input_addr,inputData.length,output_addr,frame_size,0);
var number = opus_Decode_lenght*2*2;
var output_data = new Uint8Array(number)
output_data.set(Module.HEAPU8.get(output_addr,number),number)
console.log(output_data)
Module._opus_decoder_destroy(opusDecode)