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

    if src == 'ai-connector' and dst == 'redirector' and (id == 'default' or id == 'create-connection'):
        authorized = True
    elif src == 'redirector' and dst == 'maneuvr' and id == 'default':
            authorized = True


    return authorized
    