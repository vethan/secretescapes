import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-fetch-client';
import 'fetch';

@inject(HttpClient)
export class Transactions {
  heading = 'Transactions';
  errorMessage = '';
  accountList = [];
  transactionList = [];
  accountMap = {};
  accountBalance = 0;


  constructor(http) {
    http.configure(config => {
      config
        .useStandardConfiguration()
        .withBaseUrl('http://localhost:4567/');
    });

    this.http = http;
    this.http.fetch('listAccounts')
      .then(response => response.json())
      .then(accountList => 
      {
        this.accountList = accountList;
        for(var i = 0; i < accountList.length; i++){
          this.accountMap[accountList[i]._id] = accountList[i];
        }
      });
  }

  submit() {

    this.accountBalance = this.accountMap[this.selectedPerson].balance;
    this.http.fetch('getTransactions/'+this.selectedPerson)
    .then(response => response.json())
    .then(transactionList => {
        this.transactionList = transactionList;
    }); 
  }
}
