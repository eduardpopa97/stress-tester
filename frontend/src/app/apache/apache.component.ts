import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { HttpParams } from '@angular/common/http';

import { MatTableDataSource } from '@angular/material/table';
import { SelectionModel } from '@angular/cdk/collections';
import { AlertService } from 'ngx-alerts';
import { ChartDataSets } from 'chart.js';
import { Color, Label } from 'ng2-charts';

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

export interface HTTPResponse {
  responseCode: string;
  responseMessage: string;
  sentBytes: string;
  receivedBytes: string;
  thread: string;
  url: string;
}

const HTTP_ELEM: HTTPResponse[] = [];

@Component({
  selector: 'app-apache',
  templateUrl: './apache.component.html',
  styleUrls: ['./apache.component.scss']
})

export class ApacheComponent implements OnInit {

  selectedMethod: string = '';
  elem = Array<string>();
  dataChart = Array<any>();
  response: any | null;
  stats = Array<any>();
  files = Array<any>();
  plan: any | null;
  displayResult: boolean = false;
  jsonSpring: string = '';

  displayedColumns = ['select', 'key', 'value'];
  data = Object.assign(ELEMENT_DATA);
  dataSource = new MatTableDataSource<Element>(this.data);
  selection = new SelectionModel<Element>(true, []);

  displayedColumnsLog = ['fileName', 'primary'];
  dataSource2 = Object.assign(LogDATA);
  dataSourceLog = new MatTableDataSource<LogFiles>(this.dataSource2);

  displayedColumnsHTTP = ['responseCode', 'responseMessage', 'sentBytes', 'receivedBytes', 'thread', 'url'];
  dataSource1 = Object.assign(HTTP_ELEM);
  dataSourceHTTP = new MatTableDataSource<HTTPResponse>(this.dataSource1);

  lineChartData: ChartDataSets[] = [{ data: [], label: 'Latency' }];

  lineChartLabels: Label[] = [];

  lineChartOptions = {
    responsive: false,
  };

  lineChartColors: Color[] = [
    {
      borderColor: 'black',
      backgroundColor: 'rgba(255,255,0,0.28)',
    },
  ];

  lineChartLegend = false;
  lineChartPlugins = [];
  lineChartType = 'line';

  primaryContact = { fileName: '', primary: false };

  ngOnInit(): void {
    this.http.get('http://localhost:8080/api/getPlans').subscribe(
      (result) => {
        for (let files of JSON.parse(JSON.stringify(result))) {
          this.files.push(files.planFiles);
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

  constructor(private http: HttpClient, private alertService: AlertService) {}

  primaryClick(element: LogFiles) {
    this.primaryContact = element;
  }

  onParseJMX() {
    if (this.primaryContact.fileName.length > 0) {
      let params = new HttpParams().set('test', this.primaryContact.fileName);
      this.http
        .get('http://localhost:8080/api/getPlansDetails', { params: params })
        .subscribe(
          (result) => {
            this.plan = JSON.parse(JSON.stringify(result));
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

  onSendHTTPRequest(
    users: string,
    ramp: string,
    loops: string,
    server: string,
    method: string,
    port: string,
    protocol: string,
    path: string
  ) {
    this.elem = [];
    let ok = true;
    let httpHeader = Array<any>();

    this.displayResult = true;

    const tableData = this.data.map((row: Element) => {
      return { key: row.key, value: row.value };
    });

    for (let i = 0; i < tableData.length; i++) {
      httpHeader.push(tableData[i]['key']);
      httpHeader.push(tableData[i]['value']);
    }

    if (users === '') {
      this.alertService.danger('The "Number of threads" input is empty');
      ok = false;
    } else {
      if (ramp === '') {
        this.alertService.danger('The "Ramp up period" input is empty');
        ok = false;
      } else {
        if (loops === '') {
          this.alertService.danger('The "Number of loops" input is empty');
          ok = false;
        } else {
          if (server === '') {
            this.alertService.danger('The "Server name" input is empty');
            ok = false;
          } else {
            if (typeof method === 'undefined') {
              this.alertService.danger('The "HTTP method" input is empty');
              ok = false;
            } else {
              if (path === '') {
                this.alertService.danger('The "Path" input is empty');
                ok = false;
              } 
              else {
                if(typeof protocol === 'undefined')
                {
                  this.alertService.danger('The protocol input is empty');
                  ok = false;
                }
                else {
                  if(protocol === 'https' && port != '443')
                  {
                    this.alertService.danger('The HTTPS protocol requests the port 443');
                    ok = false;
                  }
                  else {
                  ok = true;
                  }
                }
              }
            }
          }
        }
      }
    }

    if (ok == true) {
      this.alertService.info('Running the test');
      localStorage.removeItem('backend');
      let params = new HttpParams()
        .set('users', users)
        .set('ramp', ramp)
        .set('loops', loops)
        .set('server', server)
        .set('method', method)
        .set('port', port)
        .set('path', path)
        .set('protocol', protocol)
        .set('httpparam', JSON.stringify(httpHeader));

      this.http
        .get('http://localhost:8080/api/http', { params: params })
        .subscribe(
          (result) => {
            for (let backend of JSON.parse(JSON.stringify(result))) {
              this.elem.push(backend.responseCode);
              this.elem.push(backend.responseMessage);
              this.elem.push(backend.sentBytes);
              this.elem.push(backend.receivedBytes);
              this.elem.push(backend.thread);
              this.elem.push(backend.url);
              this.elem.push(backend.latency);
            }

            console.log(this.elem);
            let counter = 0;
            for (let i = 0; i < this.elem.length / 7; i++) {
              if (counter < 6) {
                HTTP_ELEM.push({
                  responseCode: this.elem[7 * i],
                  responseMessage: this.elem[7 * i + 1],
                  sentBytes: this.elem[7 * i + 2],
                  receivedBytes: this.elem[7 * i + 3],
                  thread: this.elem[7 * i + 4],
                  url: this.elem[7 * i + 5],
                });
                this.dataSourceHTTP = new MatTableDataSource<HTTPResponse>(
                  this.dataSource1
                );
                counter++;
              } else {
                counter = 0;
                this.dataChart.push(this.elem[7 * i + 6]);
              }
            }

            this.getResponse(server);
            this.getStats();
            this.getSpring();
            for (let i = 0; i < this.dataChart.length; i++) {
              this.lineChartLabels.push(i.toString());
              this.lineChartData[0].data?.push(this.dataChart[i]);
            }
            this.alertService.success('Server execution has ended');
            this.elem = [];
          },
          (error) => {
            console.log(error);
            this.alertService.warning('Backend server error');
          }
        );
    }
  }

  getResponse(server: string) {
    this.response = '';
    let params = new HttpParams().set('server', server);
    this.http
      .get('http://localhost:8080/api/getResponse', { params: params })
      .subscribe(
        (result) => {
          this.response = JSON.parse(JSON.stringify(result));
        },
        (error) => {
          console.log(error);
          this.alertService.warning('Backend server error');
        }
      );
  }

  getStats() {
    this.http.get('http://localhost:8080/api/getStats').subscribe(
      (result) => {
        for (let stats of JSON.parse(JSON.stringify(result))) {
          this.stats.push(stats.tests);
          this.stats.push(stats.throughput);
          this.stats.push(stats.average);
          this.stats.push(stats.minimum);
          this.stats.push(stats.maximum);
        }
      },
      (error) => {
        console.log(error);
        this.alertService.warning('Backend server error');
      }
    );
  }

  getSpring() {
    this.http.get('http://localhost:8080/api/spring').subscribe(
        (result) => {
          this.jsonSpring = JSON.stringify(result, undefined, 2);
        },
        (error) => {
          console.log(error);
          this.alertService.warning('Backend server error');
        }
      );
  }


}
