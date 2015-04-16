/**
 * Created by Umair on 4/15/2015.
 */
public class Result
{
    private String _message;

    public Result(String message)
    {
        _message = message;
    }

    public Result(int code)
    {
        if(code >= 0)
            _message = Constant.SUCCESS;
        else
            _message = Constant.FAIL;
    }

    @Override
    public String toString()
    {
        if(_message != null)
            return _message;

        return "";
    }
}
