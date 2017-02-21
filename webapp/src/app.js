export class App {
  configureRouter(config, router) {
    config.title = 'Aurelia';
    config.map([
      { route: ['', 'transactions'], name: 'transactions',      moduleId: 'transactions',      nav: true, title: 'Transactions' },
      { route: 'payment',         name: 'payment',        moduleId: 'payment',        nav: true, title: 'Payment' },
    ]);

    this.router = router;
  }
}
