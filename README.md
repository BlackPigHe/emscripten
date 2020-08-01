# emscripten
使用emscripten打包一个完整的项目，作为前端js的解码工具。c库为开源的opus。操作详细介绍，知识前提docker，c++编译，基础的emscripten的认知，js的基础知识


Welcome to the emscripten wiki!
该项目将指导新手玩家怎么将一个**opus音频库**，编译为js，并运行在**浏览器中。**
# emscripten环境搭建
1. 由于emscripten的环境搭建需要很多安装包下载速度极慢，这里推介大家使用**docker镜像**来编译
   * 这里推介两个镜像 **emscripten/emsdk**  以及  **trzeci/emscripten**
   * 国内的镜像地址使用阿里云的镜像源，速度很快（这里不详细介绍，给出daemon.json）


     {
      "registry-mirrors": ["https://x9o4p9lt.mirror.aliyuncs.com"]
     }


   * pull 命令也不说了，注意镜像名字不打错就行
   
2. 进入编译环境
   * 通过docker run 启动一个设备，我的叫emscripten 
   * `docker exec -it emscripten bash`  (注意这里的emscripten是我的docker设备名字，你需要改成你的)
3. 下载opus的源码，直接github上搜索即可
   * 我这里是直接将源码通过docker cp考入了我的docker设备中（命令很简单，不细说）
4. 重头戏来了，开始编译
   * 这里需要对官网文档的使用进行补充说明，由于咋们使用的是虚机，
      1. 编译的时候会抱一个获取不到cpu信息的错误，导致Makefile无法生成，这里需要特别注意一下
      2. 编译环境也有可能会缺失./configure 的环境，这里apt-get 对应的环境即可（百度一查一大堆，不累诉）
   * 生成Makefile文件的命令


      `emconfigure ./configure CFLAGS="-O3" --disable-extra-programs --disable-rtcd`
   * make打包生成so和a文件


       `emmake make`
   * 在当前目录下的.lib查看生成的.so .a文件，还需要查看项目中的.o文件类型是否是llvm的


       `file src/opus_decode.o`
   * emcc将libopus.a打包为js


       `emcc .libs/libopus.a -o decoder.js`  (这是最简单的形式，还需要添加导出函数和运行时函数来生成胶水js，方便加载wasm)
       简单官方emcc 参数说明：


           -s 参数说明https://github.com/emscripten-core/emscripten/blob/master/src/settings.js
           例如：-s EXPORTED_RUNTIME_METHODS=[] 导出运行函数，可以操作wasm进行运行时的内存读写
                -s EXPORTED_FUNCTIONS=[]  导出编译的c++的库中你想要的函数。我这里只需要decode相关函数，
                -s ALLOW_MEMORY_GROWTH=1  运行内存扩充
      最终编译命令为


       * emcc -O0 .libs/libopus.a -s EXPORTED_RUNTIME_METHODS="['_opus_decode','_opus_decoder_create','_opus_decoder_destroy']" \
                                -s EXPORTED_RUNTIME_METHODS="['cwrap','getValue','setValue']" \
                                -s ALLOW_MEMORY_GROWTH=1 \
                                -o decoder.js
5. 进行胶水代码加载wasm
    * 需要注意利用流加载的方式，需要设置响应头 Content-Type：application/wasm
    * 关键问题使用Module进行内存读写和借口调用，代码上传中。。。

   
