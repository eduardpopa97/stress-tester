import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { HttpParams } from '@angular/common/http';

import { MatTableDataSource } from '@angular/material/table';
import { SelectionModel } from '@angular/cdk/collections';
import { AlertService } from 'ngx-alerts';

export interface LogFiles {
  fileName: string;
  primary: boolean;
}

const LogDATA: LogFiles[] = [];

export interface Element {
  key: string;
  value: string;
}

const ELEMENT_DATA: Element[] = [{ key: '', value: '' }];

@Component({
  selector: 'app-k6',
  templateUrl: './k6.component.html',
  styleUrls: ['./k6.component.scss']
})
export class K6Component implements OnInit {

  constructor(private http: HttpClient, private alertService: AlertService) { }

  ngOnInit(): void {
    this.http.get('http://localhost:8080/api/getK6Plans').subscribe(
      (result) => {
        for (let files of JSON.parse(JSON.stringify(result))) {
          this.files.push(files.planK6Files);
        }

        for (let i = 0; i < this.files.length; i++) {
          LogDATA.push({ fileName: this.files[i], primary: false });
          this.dataSourceLog = new MatTableDataSource<LogFiles>(
            this.dataSource2
          );
        }
      },
      (error) => {
        console.log(error);
        this.alertService.warning('Backend server error');
      }
    );
  }

  plan: any | null;
  selectedPlan: any | null;
  files = Array<any>();

  displayedColumns = ['select', 'key', 'value'];
  data = Object.assign(ELEMENT_DATA);
  dataSource = new MatTableDataSource<Element>(this.data);
  selection = new SelectionModel<Element>(true, []);

  primaryContact = { fileName: '', primary: false };

  primaryClick(element: LogFiles) {
    this.primaryContact = element;
  }

  onParseK6() {
    if (this.primaryContact.fileName.length > 0) {
      let params = new HttpParams().set('test', this.primaryContact.fileName);
      this.http
        .get('http://localhost:8080/api/getK6PlansDetails', { params: params })
        .subscribe(
          (result) => {
            this.selectedPlan = JSON.parse(JSON.stringify(result));
          },
          (error) => {
            console.log(error);
            this.alertService.warning('Backend server error');
          }
        );
    } else {
      this.alertService.warning("You haven't selected a plan file");
    }
  }

  displayedColumnsLog = ['fileName', 'primary'];
  dataSource2 = Object.assign(LogDATA);
  dataSourceLog = new MatTableDataSource<LogFiles>(this.dataSource2);


  /** Whether the number of selected elements matches the total number of rows. */
  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.data.length;
    return numSelected === numRows;
  }
  removeSelectedRows() {
    this.selection.selected.forEach((item) => {
      let index: number = this.data.findIndex((d: any) => d === item);
      console.log(this.data.findIndex((d: any) => d === item));
      this.data.splice(index, 1);
      this.dataSource = new MatTableDataSource<Element>(this.data);
    });
    this.selection = new SelectionModel<Element>(true, []);
  }
  /** Selects all rows if they are not all selected; otherwise clear selection. */
  masterToggle() {
    this.isAllSelected()
      ? this.selection.clear()
      : this.dataSource.data.forEach((row) => this.selection.select(row));
  }

  addRows() {
    this.dataSource.data.push({ key: '', value: '' });
    this.dataSource = new MatTableDataSource<Element>(this.data);
  }

  runK6(URL: string) {

    let ok = true;
    
    let httpHeader = Array<any>();

    const tableData = this.data.map((row: Element) => {
      return { key: row.key, value: row.value };
    });

    for (let i = 0; i < tableData.length; i++) {
      httpHeader.push(tableData[i]['key']);
      httpHeader.push(tableData[i]['value']);
    }

    for(let i = 0; i < httpHeader.length; i++) {
      if(httpHeader[i] == "") ok = false;
    }

    if(URL === '' || ok == false)
    {
      this.alertService.danger('You have not completed the necessary information!');
    }
    else 
    {
      this.alertService.info('Running the test');
      let params = new HttpParams()
        .set('url', URL)
        .set('load', JSON.stringify(httpHeader));

      this.http.get('http://localhost:8080/api/k6', {params : params}).
      subscribe(
        (result) => {
          this.plan = (JSON.parse(JSON.stringify(result)));
          console.log(result);
        }
      )  
    
    }
  }

}
