import { Component, OnInit } from '@angular/core';
import { RecordService } from "../shared/record/record.service";

@Component({
  selector: 'app-record-list',
  templateUrl: './record-list.component.html',
  styleUrls: ['./record-list.component.css']
})
export class RecordListComponent implements OnInit {
  records: Array<any>;
  file: any;

  fileChanged(e) {
    this.file = e.target.files[0];
  }
  constructor(private recordService: RecordService) { }

  ngOnInit() { }

  uploadDocument() {
    this.records = Array<any>();
    const fileReader = new FileReader();
    fileReader.readAsText(this.file);
    fileReader.onload = (e) => {


      this.recordService.sendRecords(fileReader.result.toString(), this.file.name.split('.').pop()).subscribe(x => {
        this.records = x;
      });
    }
    //console.log();
    //console.log(this.recordService;

  }

}
