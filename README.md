# CSV upload/download service

I would prefer to operate with JSON data in the service, but the requirement is to use CSV files for both input and output.
I asked a question about this, but did not get a response in time.

## Example curl commands to use the service
```
curl -X POST -F "file=@exercise.csv" http://localhost:8080/measurement
curl -X GET http://localhost:8080/measurement
curl -X GET http://localhost:8080/measurement/415922000
curl -X DELETE http://localhost:8080/measurement
```

## What can be improved
- Date fields (Strings at the moment)
- Sorting according to sortingPriority field
- Pagination
- Error handling
- Logging

Had not enough time to implement all of this. I choose to add unit tests instead.
