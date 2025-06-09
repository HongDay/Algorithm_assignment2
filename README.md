# 📘 CSE331 - Assignment 2 (25-1)

**20201325 홍대의**  
This repository is for **CSE331: Introduction to Algorithms - Assignment2**

<br/>

## 📂 Repository Overview

### Algorithm_assignment2
- `Main.java` : `.tsp`데이터 로드 및 알고리즘 실행, 결과 파일 및 tour시각화 반환
- `makePng.java` : 결과 tour를 시각화해서 생성
- `makeTour.java` : `.tour` 파일로 결과파일 생성
- `TspParser.java` : `.tsp` 파일을 알고리즘에 input가능한 형태로 변환
- `GtCost.java` : Ground Truth tour파일의 cost 계산
- algorithms
    - `ClrsApx.java` : `ApxTspTour()` 알고리즘 함수 코드
    - `HeldKarp.java` :  `HeldKarpTour()` 알고리즘 함수 코드
    - `Greedy.java` : `GreedyInsertion()` 알고리즘 함수 코드
    - `MyOwn.java` : 3가지 novel 알고리즘 함수 코드 - `HKGreedySubTour()`, `HKDivideConquer()`, `HKMSTHybrid()`
- dataset
    - ulysses16.tsp
    - ulysses16.opt.tour
    - ulysses22.tsp
    - ulysses22.opt.tour
    - a280.tsp
    - a280.opt.tour
    - xql662.tsp
    - xql662.tour
    - kz9976.tsp
    - kz9976.tour
    - mona-lisa100K.tsp
- output : 결과 tour파일과 png파일 저장
- Result_screenshots : 실행결과 터미널 화면 캡쳐사진 저장