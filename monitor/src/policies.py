def check_operation(id, headers):
    authorized = False
    # print(f"[debug] checking policies for event {id}, headers: {headers}")
    try:    
        print(f"[info] checking policies for event {id},"
            f" {headers['from']}->{headers['to']}")
    except:
        print("No key or incorrect headers' content.")
        return False
    
    src = headers['from']
    dst = headers['to']

    if src == 'ai-connector' and dst == 'redirector' and (id == 'default'):
        authorized = True
    elif src == 'ai-connector' and dst == 'jarvis' and id == 'default':
        authorized = True
    elif src == 'ai-connector' and dst == 'geo' and id == 'default':
        authorized = True
    elif src == 'jarvis' and dst == 'ai-connector' and id == 'default':
        authorized = True
    elif src == 'enemy' and dst == 'weapon' and id == 'default':
        authorized = True
    elif src == 'geo' and dst == 'monitoring' and id == 'default':
        authorized = True
    elif src == 'geo' and dst == 'stabilizer' and id == 'default':
        authorized = True
    elif src == 'monitoring' and dst == 'interface' and id == 'default':
        authorized = True
    elif src == 'monitoring' and dst == 'monitoring' and id == 'default':
        authorized = True
    elif src == 'monitoring' and dst == 'redirector' and id == 'default':
        authorized = True
    elif src == 'redirector' and dst == 'ai-connector' and id == 'default':
        authorized = True
    elif src == 'redirector' and dst == 'monitoring' and id == 'default':
        authorized = True
    elif src == 'redirector' and dst == 'travel' and id == 'default':
        authorized = True
    elif src == 'redirector' and dst == 'weapon' and id == 'default':
        authorized = True
    elif src == 'stabilizer' and dst == 'stabilizer' and id == 'default':
        authorized = True
    elif src == 'travel' and dst == 'geo' and id == 'default':
        authorized = True
    elif src == 'weapon' and dst == 'enemy' and id == 'default':
        authorized = True
    elif src == 'weapon' and dst == 'weapon' and id == 'default':
        authorized = True

    return authorized
    