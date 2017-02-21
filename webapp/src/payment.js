//import {computedFrom} from 'aurelia-framework';
import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-fetch-client';
import 'fetch';

@inject(HttpClient)
export class Payment {
  heading = 'Payment';
  firstName = 'John';
  lastName = 'Doe';
  selectedAmount = 20;
  errorMessage = '';
  accountList = [];
  //Getters can't be directly observed, so they must be dirty checked.
  //However, if you tell Aurelia the dependencies, it no longer needs to dirty check the property.
  //To optimize by declaring the properties that this getter is computed from, uncomment the line below
  //as well as the corresponding import above.
  //@computedFrom('firstName', 'lastName')
 /* get fullName() {
    return `${this.firstName} ${this.lastName}`;
  }*/
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

    var transferInfo = { fromAccount: this.selectedFrom, toAccount: this.selectedTo,amount: Number(this.selectedAmount) };
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

  canDeactivate() {
    if (this.fullName !== this.previousValue) {
      return confirm('Are you sure you want to leave?');
    }
  }
}

export class UpperValueConverter {
  toView(value) {
    return value && value.toUpperCase();
  }
}
