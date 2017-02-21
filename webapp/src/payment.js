//import {computedFrom} from 'aurelia-framework';
import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-fetch-client';
import 'fetch';

@inject(HttpClient)
export class Payment {
  heading = 'Payment';
  selectedAmount = 20;
  errorMessage = '';
  accountList = [];

  constructor(http) {
    http.configure(config => {
      config
        .useStandardConfiguration()
        .withBaseUrl('http://localhost:4567/');
    });

    this.http = http;
    this.http.fetch('listAccounts')
      .then(response => response.json())
      .then(accountList => this.accountList = accountList);
  }
  

  submit() {
    if(this.selectedTo == this.selectedFrom)
    {
      alert(`Accounts must be different during transfer!`);
      return;
    }

    var transferInfo = { fromAccount: this.selectedFrom, toAccount: this.selectedTo,amount: Number(this.selectedAmount)*100 };
    this.http.fetch('makePayment', {
        method: 'post',
        body: JSON.stringify(transferInfo)
    }).then(response => response.json())
      .then(responseObject => {
        if(!responseObject.success)
        {
          this.errorMessage = responseObject.errorMessage;
        }
        else
        {
          this.errorMessage = "Transfer completed successfully";
        }
    });     
  }

}

