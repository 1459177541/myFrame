package FactoryAndAOP;


import proxy.annotation.Async;

public interface AutowiredModel3Interface {
    @Async
    void testAsync();
}
