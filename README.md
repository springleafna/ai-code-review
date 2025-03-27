ProcessBuilder 是 Java 中用于启动和管理外部进程的类。
ProcessBuilder类是J2SE 1.5在java.lang中新添加的一个新类，此类用于创建操作系统进程，它提供一种启动和管理进程（也就是应用程序）的方法。  
在J2SE 1.5之前，都是由Process类处来实现进程的控制管理。每个 ProcessBuilder 实例管理一个进程属性集。  
它的start() 方法利用这些属性创建一个新的 Process 实例。start() 方法可以从同一实例重复调用，以利用相同的或相关的属性创建新的子进程。  
常用方法：  
- void command(String... command) 用于将待执行命令及参数传递给它；  

- Process start() 执行命令并返回一个Process对象，用于获取对执行程序的输入和输出；  

- void directory(File base) 用于设置待执行命令的工作目录，可以不设置；  

它可以：  
- 执行系统命令（如 git、ls 等）。  
- 设置命令的工作目录、环境变量。  
- 重定向输入/输出流。  
- 等待进程执行完成。  

为什么用 ProcessBuilder 而不是直接 Runtime.exec()？  
- 更安全的参数处理（避免 Shell 注入风险）。  
- 更灵活的重定向和目录控制。  
- 更清晰的 API 设计。  