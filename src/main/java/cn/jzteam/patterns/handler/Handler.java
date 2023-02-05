package cn.jzteam.patterns.handler;

public abstract class Handler {
	// 下一个责任链节
	private Handler nextHandler;
	// 处理请求
	public final Response handleMessage(Request request){
		Response response = null;
		if(this.getHandlerLevel().equals(request.getRequestLevel())){
			response = this.response();
		}else{
			if(this.nextHandler != null){
				response = this.nextHandler.handleMessage(request);
			}else{
				//没有适当的处理者，业务自行处理
			}
		}
		return response;
	}
	// 设置子类在责任链中的位置（顺序）
	protected abstract Level getHandlerLevel();
	// 子类处理自己对应的业务
	protected abstract Response response();
}
