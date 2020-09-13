# finch-zio-slick #

Test ground for integration between finch, ZIO and Slick


## wrk tests (MacBook Pro 2016 2,6 GHz Quad-Core Intel Core i7)

Best time in 5 attempts for fetching a single Todo:

```
wrk -t4 -c400 -d30s --timeout 10s --latency http://127.0.0.1:8000/api/todos/1

Running 30s test @ http://127.0.0.1:8000/api/todos/1
  4 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   130.86ms   23.30ms 269.23ms   79.67%
    Req/Sec   743.38     97.28     1.16k    69.42%
  Latency Distribution
     50%  134.82ms
     75%  144.52ms
     90%  153.76ms
     99%  177.83ms
  88814 requests in 30.02s, 26.34MB read
  Socket errors: connect 0, read 320, write 0, timeout 0
Requests/sec:   2958.26
Transfer/sec:      0.88MB
```

Best time in 5 attempts for fetching 50 Todo's:

```
wrk -t4 -c400 -d30s --timeout 10s --latency http://127.0.0.1:8000/api/todos

Running 30s test @ http://127.0.0.1:8000/api/todos
  4 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   239.38ms   37.18ms 412.43ms   76.28%
    Req/Sec   410.33     70.14   710.00     66.75%
  Latency Distribution
     50%  245.57ms
     75%  262.48ms
     90%  277.25ms
     99%  311.98ms
  49074 requests in 30.04s, 633.54MB read
  Socket errors: connect 0, read 247, write 0, timeout 0
Requests/sec:   1633.42
Transfer/sec:     21.09MB
```

## Contribution policy ##

Contributions via GitHub pull requests are gladly accepted from their original author. Along with
any pull requests, please state that the contribution is your original work and that you license
the work to the project under the project's open source license. Whether or not you state this
explicitly, by submitting any copyrighted material via pull request, email, or other means you
agree to license the material under the project's open source license and warrant that you have the
legal authority to do so.

## License ##

This code is open source software licensed under the
[MIT](https://opensource.org/licenses/MIT) license.
