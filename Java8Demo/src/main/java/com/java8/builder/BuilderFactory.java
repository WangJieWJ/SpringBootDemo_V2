package com.java8.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Title: 
 * Description: 
 * Copyright: 2020 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: SpringBootDemo
 * Author: 王杰
 * Create Time:2020-01-16 17:22
 */
public class BuilderFactory<T> {


	private final Supplier<T> instantiator;

	private List<Consumer<T>> modifiers = new ArrayList<>();

	public BuilderFactory(Supplier<T> instantiator) {
		this.instantiator = instantiator;
	}

	public static <T> BuilderFactory<T> of(Supplier<T> instantiator) {
		return new BuilderFactory<>(instantiator);
	}

	public <P1> BuilderFactory<T> with(Consumer1<T, P1> consumer, P1 p1) {
		Consumer<T> c = instance -> consumer.accept(instance, p1);
		modifiers.add(c);
		return this;
	}

	public <P1, P2> BuilderFactory<T> with(Consumer2<T, P1, P2> consumer, P1 p1, P2 p2) {
		Consumer<T> c = instance -> consumer.accept(instance, p1, p2);
		modifiers.add(c);
		return this;
	}

	public <P1, P2, P3> BuilderFactory<T> with(Consumer3<T, P1, P2, P3> consumer, P1 p1, P2 p2, P3 p3) {
		Consumer<T> c = instance -> consumer.accept(instance, p1, p2, p3);
		modifiers.add(c);
		return this;
	}

	public T build() {
		T value = instantiator.get();
		modifiers.forEach(modifier -> modifier.accept(value));
		modifiers.clear();
		return value;
	}

	/**
	 * 1 参数 Consumer
	 */
	@FunctionalInterface
	public interface Consumer1<T, P1> {
		void accept(T t, P1 p1);
	}

	/**
	 * 2 参数 Consumer
	 */
	@FunctionalInterface
	public interface Consumer2<T, P1, P2> {
		void accept(T t, P1 p1, P2 p2);
	}

	/**
	 * 3 参数 Consumer
	 */
	@FunctionalInterface
	public interface Consumer3<T, P1, P2, P3> {
		void accept(T t, P1 p1, P2 p2, P3 p3);
	}
}
