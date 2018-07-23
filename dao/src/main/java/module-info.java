module myFrame.dao {
    exports dao.service;
    exports dao.beanBuffer;

    requires myFrame.frame;
    requires myFrame.asynchronous;

}