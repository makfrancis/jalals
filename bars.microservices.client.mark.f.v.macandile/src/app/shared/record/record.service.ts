import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
@Injectable({
  providedIn: 'root'
})
export class RecordService {

  constructor(private http: HttpClient) { }
  hasError = false;
  errMessage = '';



  sendRecords(requests: string, fileType: string): Observable<any> {
    this.errMessage = '';
    this.hasError = false;
    // if (!(fileType === 'csv') && !(fileType === 'text')) {
    //   this.hasError = true;
    //   this.errMessage = "File not supported";
    // }
    // else {
    if (fileType === 'csv' || fileType === 'txt') {
      return this.http.post('http://alcd-g88nrk2-dx.dir.svc.accenture.com:8765/bars.microservices.server/' + fileType, requests).pipe(
        catchError(err => {
          this.errMessage = err.error.message;
          if (this.errMessage == undefined) {
            this.errMessage = "Server is down";
          }
          this.hasError = true;
          console.error(this.errMessage);

          return throwError(err);
        })
      );
    }
    else {
      this.hasError = true;
      this.errMessage = "File not supported";
    }
  }

}
