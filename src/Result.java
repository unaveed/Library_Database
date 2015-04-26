/**
 * Created by Umair on 4/15/2015.
 */
public class Result
{
    private String _message;
    private boolean _validData;
    public Result(String message)
    {
        _message = message;
        if(_message.equals(Constant.BAD_REQUEST) || _message.equals(Constant.FAIL))
            _validData = false;
        else
            _validData = true;
    }

    public Result(int code)
    {
        if(code >= 0)
        {
            _message = Constant.SUCCESS;
            _validData = true;
        }
        else
        {
            _message = Constant.FAIL;
            _validData = false;
        }
    }

    public boolean isValid()
    {
        return _validData;
    }

    @Override
    public String toString()
    {
        if(_message != null)
            return _message;

        return "";
    }
}
