### Pace Monitor
Live monitoring of running pace (minutes per mile) calculated in three ways using the previous 5, 30 and 60 seconds of speed data from device GPS

### Libraries Used
- Karumi: for permission requests
- FusedLocationProvider: for speed monitoring
- EventBus: used by service to post speed reports
- ̶R̶x̶J̶a̶v̶a̶,̶ ̶m̶o̶s̶t̶l̶y̶ ̶j̶u̶s̶t̶ ̶P̶u̶b̶l̶i̶s̶h̶S̶u̶b̶j̶e̶c̶t̶s̶ ̶a̶n̶d̶ ̶C̶o̶n̶s̶u̶m̶e̶r̶s̶ ̶f̶o̶r̶ ̶h̶a̶n̶d̶l̶i̶n̶g̶ ̶t̶h̶e̶ ̶s̶t̶r̶e̶a̶m̶ ̶o̶f̶ ̶s̶p̶e̶e̶d̶ ̶r̶e̶p̶o̶r̶t̶s̶ ̶f̶r̶o̶m̶ ̶t̶h̶e̶ ̶s̶e̶r̶v̶i̶c̶e̶ ̶a̶n̶d̶ ̶u̶p̶d̶a̶t̶i̶n̶g̶ ̶t̶h̶e̶ ̶U̶I̶
- LiveData: Handling the stream of speed data from the service to the ui (initially implemented with rxjava)
