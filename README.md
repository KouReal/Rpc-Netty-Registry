# Rpc-Netty-Registry
20190823
rpc系统修改记录
博客图文：https://www.cnblogs.com/CreatorKou/p/11402277.html

将httpserver模块的httptask去掉，直接在httpserverhandler调用rpcproxy的call的rpcclient的invokewithfuture的ctx的writeandflush，，这一系列调用过程都是不会阻塞的，除了第一次调用某个服务创建rpcclient是有阻塞cpu的，之后就全在线程内执行，不参与io网络传输。
为什么要去掉httptask，因为httptask是线程，意味着每次从httpserver接受到一个请求，都会创建一个httptask线程，而这个httptask线程会根据future使自己waitting，parknonos，而线程数量不能创建的太多，尤其不能和请求数量正比例增长，因为线程的来回切换会得不偿失，但是如果用线程池来对付请求，也不行，因为线程池数量有限，并且在线程池的线程里等待网络io必然会耗费时间，这和传统bio问题是一样的，这个线程池也就无法满足高并发请求，总之，高并发不意味着线程必须多，反而是需要线程的充分利用，追求并行而不是并发，所以是和实际物理cpu并行能力来决定的，在双核cpu，一般双核可以支持并行4线程，所以netty的workergroup默认线程数是4，虽然只有这4个线程，但是如果能做到这4个线程充分并行利用，不阻塞不等待网络读写io，就可以达到请求量的最大效果。

注意，这里说的bio或者nio指的是网络socket的io，对于磁盘的io是没用的，因为网络io可以通过buffer缓存异步处理读写，然后应用层轮询到处理完成的io通道，可以不用等待网络传输的延时，拿到现成的就可以来用，达到非阻塞的目的。

而磁盘io必须经过内核态和用户态拷贝，都需要cpu和内存和磁盘的参与，比如数据库select语句需要比较字段，而比较这个操作需要cpu内部逻辑单元参与。

至于registryclient的discover就必须阻塞请求线程，因为发起discover请求的rpcproxy只有拿到远程服务的地址才能继续创建rpcclient，这个请求不会太频繁，属于一劳永逸。

registryclient的addservice请求也需要阻塞，因为只有收到注册中心的normalconfig才能继续springboot环境中其他配置类的参数设置，完成bean的创建，只有在启动的时候才会发这个请求。

而在rpcserver需要创建线程池来处理rpc请求，这个线程池主要执行数据库读写，又因为读写请求各不相同，有的需要时间长，有的需要时间短，为了平衡时间和满足各个请求的公平，使用适当大小的线程池来处理数据库读写，有几率将耗时短的任务排到前列，根据最优服务次序问题的贪心算法，耗时短的任务排到前排可以减少平均等待时间，这里模糊利用了多线程来平衡和近似，但不够严谨，是否可以改进，在一段时间积累到多个读写数据库的任务之后，根据io类型预估时间进行排序然后穿行执行达到最优效率。那么在收集io请求期间这个动作是不参与io的，可以和其他方面的线程并发进行。

数据库读写也就是磁盘也就是磁盘io，这方面没法实现这方面没法实现nio，性能受限于数据库性能，所以需要优化数据库，分库分表。

cpu利用率是专业术语，是指cpu工作强度的程度，cpu被io阻塞多了，利用率反而不会升高，而高并发的目的就是为了充分提高cpu利用率，不让它等待io或者因为io而导致cpu频繁的切换，浪费在没有实际意义的事情上。所以在追求高并发的话题下，cpu要追求实际作用的高利用率，偏爱于让高并发请求吃满Cpu，而且不给他喘息或者切换的机会。

贪心算法是什么原理？
贪心(多处最优服务次序问题)
静静的追逐阅读数：54052016-04-22
版权声明：本文为博主原创文章，遵循 CC 4.0 by-sa 版权协议，转载请附上原文出处链接和本声明。
本文链接：https://blog.csdn.net/jingttkx/article/details/51221097
问题：

设有N个顾客同时等待一项服务，顾客i需要的服务时间为ti（1<=i<=N）共有S处可以提供此项服务，

应如何安排N个顾客需要的服务次序才能使平均等待时间达到最小？平均等待时间等于N个顾客等待服务的总时间除以N。

输入：

第一行两个正整数N和S表示N个顾客S处服务，接下来N个顾客需要的服务时间

输出：

平均等待时间，保留3位小数。

输入样例：

10 2

56 12 1 99 1000 23433 55 99 812

输出样例：

336.000



顾客的等待时间n个顾客，s个服务取n个顾客做药服务的时间，求平均等待是时间达到最小（服务时间从小到大排序）



#include <iostream>
#include <algorithm>
#include <cstdio>
#include <cstring>
#include <string>

using namespace std;
int a[1000];
int ser[100];//服务窗口的顾客等待时间
int sum[100];//服务窗口顾客等待时间的总和。
int main()
{
    int n,s;
    while(~scanf("%d%d",&n,&s))
    {
        for(int i=0;i<n;i++)
        {
         scanf("%d",&a[i]);
        }
        sort(a,a+n);
        memset(ser,0,sizeof(ser));
        memset(sum,0,sizeof(sum));
        int i=0;
        int j=0;
        while(i<n)
        {
            ser[j]+=a[i];
            sum[j]+=ser[j];
            i++,j++;
            if(j==s)
                j=0;

        }
        double t=0;
        for(int i=0;i<n;i++)
        {
            t+=sum[i];
        }
        t/=n;
        printf("%0.3lf\n",t);
    }
    return 0;
}
