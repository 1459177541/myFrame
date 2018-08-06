module myFrame.dao {
    exports dao.service;
    exports dao.beanBuffer;

    uses factory.BeanFactoryHandler;

    requires myFrame.frame;
    requires myFrame.asynchronous;

}