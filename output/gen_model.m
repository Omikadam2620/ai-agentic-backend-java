function gen_model()
    model = 'auto_model';
    if bdIsLoaded(model)
        close_system(model, 0);
    end
    if exist([model, '.slx'], 'file')
        delete([model, '.slx']);
    end
    new_system(model);

    % Layout
    x_in = 50;
    x_runnable = 200;
    x_out = 350;
    y_start = 50;
    y_step = 40;

    input_blocks = {'LvEisExctCdnVld', 'LvEisMesEnd', 'SetFrqCntctCtlEis', 'SetLvCntctEisClsCmd', 'SetDucyCntctCtlEis'};
    output_blocks = {'LvChaDcvCpLinkEisRdy', 'LvChaDcvCpLinkEis', 'LvActExctAmpEis', 'LvEisExctAct', 'LvCaprDcvLinkChaToutEis', 'LvEisExctActExt', 'LvEisExctActExtCdn', 'DC link capacitor charging feedback', 'DC link charging feedback timeout', 'TiCalChgDcvCpLinkEis', 'TiCalDlyActExctEis'};
    n_inputs = numel(input_blocks);
    n_outputs = numel(output_blocks);

    % Subsystem (Runnable)
    add_block('simulink/Ports & Subsystems/Subsystem', [model, '/Runnable'], ...
        'Position', [x_runnable, y_start, x_runnable+60, y_start + max(n_inputs, n_outputs)*y_step]);
    delete_block([model, '/Runnable/In1']);
    delete_block([model, '/Runnable/Out1']);

    % Add Inports to Runnable
    for idx = 1:n_inputs
        y = y_start + (idx-1)*y_step;
        add_block('simulink/Ports & Subsystems/In1', [model, '/Runnable/In', num2str(idx)], ...
            'Position', [30, y, 60, y+20]);
    end

    % Add Outports to Runnable
    for idx = 1:n_outputs
        y = y_start + (idx-1)*y_step;
        add_block('simulink/Ports & Subsystems/Out1', [model, '/Runnable/Out', num2str(idx)], ...
            'Position', [160, y, 190, y+20]);
    end

    % Top-level Inputs
    for idx = 1:n_inputs
        name = input_blocks{idx};
        y = y_start + (idx-1)*y_step;
        add_block('simulink/Sources/In1', [model, '/', name], ...
            'Position', [x_in, y, x_in+30, y+20]);
        add_line(model, [name, '/1'], ['Runnable/', num2str(idx)]);
    end

    % Top-level Outputs
    for idx = 1:n_outputs
        name = output_blocks{idx};
        y = y_start + (idx-1)*y_step;
        add_block('simulink/Sinks/Out1', [model, '/', name], ...
            'Position', [x_out, y, x_out+30, y+20]);
        add_line(model, ['Runnable/', num2str(idx)], [name, '/1']);
    end

    save_system(model);
    open_system(model);
    set_param(model, 'Open', 'on');
    set_param(model, 'ZoomFactor', 'FitSystem');
    disp('Model created and opened.');
end