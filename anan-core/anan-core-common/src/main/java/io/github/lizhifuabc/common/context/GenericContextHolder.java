package io.github.lizhifuabc.common.context;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.Objects;
import java.util.Optional;

/**
 * 通用上下文
 *
 * @author lizhifu
 * @since 2025/4/16
 */
public class GenericContextHolder<T> {
    private final TransmittableThreadLocal<T> context = new TransmittableThreadLocal<>();

    /**
     * 获取上下文
     *
     * @return null or 上下文
     */
    public T get() {
        return context.get();
    }

    /**
     * 设置当前上下文
     *
     * @param t 新上下文，传 null 则为清除
     */
    public void set(T t) {
        if (Objects.isNull(t)) {
            clear();
        } else {
            context.set(t);
        };
    }

    /**
     * 强制清空本线程的上下文，防止影响被线程池复用的其他线程，以及内存泄露
     */
    public void clear() { context.remove(); }

    /**
     * 获取上下文
     *
     * @return null or 上下文
     */
    public Optional<T> getOptional() { return Optional.ofNullable(context.get()); }
}
